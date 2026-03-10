#!/bin/bash
set -euo pipefail

DRY_RUN=false
if [[ "${1:-}" == "--dry-run" ]]; then
    DRY_RUN=true
    echo "Dry-run mode: no changes will be made."
    echo ""
fi

# Navigate to project root (parent of scripts/)
cd "$(cd "$(dirname "$0")" && pwd)/.."

# Git safety check: warn if there are uncommitted changes.
# Skipped in dry-run since no files will be modified.
if ! "$DRY_RUN" && git rev-parse --git-dir >/dev/null 2>&1 && [ -n "$(git status --porcelain)" ]; then
    echo "Warning: uncommitted changes detected. Stash or commit first so you"
    echo "can recover if something goes wrong."
    echo ""
    read -rp "  s = stash automatically, c = continue anyway, n = cancel: " GIT_CONFIRM
    case "$GIT_CONFIRM" in
        s) git stash push -u -m "pre-package-rename stash" && echo "  Stashed." ;;
        c) ;;
        *) echo "Cancelled."; exit 0 ;;
    esac
    echo ""
fi

validate_package() {
    local name="$1" label="$2"
    if [ -z "$name" ]; then
        echo "Error: $label cannot be empty"
        exit 1
    fi
    if ! echo "$name" | grep -qE '^[a-zA-Z][a-zA-Z0-9_]*(\.[a-zA-Z][a-zA-Z0-9_]*)+$'; then
        echo "Error: '$name' is not a valid package name (e.g., nl.q42.template)"
        exit 1
    fi
}

read -rp "Enter the current package name (e.g., nl.q42.template): " OLD_PACKAGE
validate_package "$OLD_PACKAGE" "Current package name"

read -rp "Enter the new package name (e.g., com.example.app): " NEW_PACKAGE
validate_package "$NEW_PACKAGE" "New package name"

if [ "$OLD_PACKAGE" = "$NEW_PACKAGE" ]; then
    echo "Error: old and new package names are identical"
    exit 1
fi

echo ""
echo "  Old: $OLD_PACKAGE"
echo "  New: $NEW_PACKAGE"
echo ""
read -rp "Continue? (y/n): " CONFIRM
[ "$CONFIRM" = "y" ] || { echo "Cancelled."; exit 0; }
echo ""

# OS-portable sed in-place flag
if [[ "$OSTYPE" == "darwin"* ]]; then
    SED_INPLACE=(-i '')
else
    SED_INPLACE=(-i)
fi

# OLD_ESCAPED: dots escaped for use as a regex *pattern* in sed's search side.
# NEW_PACKAGE is used directly on the replacement side — the validator above
# guarantees the package only contains letters, digits, dots, and underscores
# ([a-zA-Z0-9_.]), so no sed replacement metacharacters (\ or &) can appear.
OLD_ESCAPED="${OLD_PACKAGE//./\\.}"
OLD_PATH=$(tr '.' '/' <<< "$OLD_PACKAGE")
NEW_PATH=$(tr '.' '/' <<< "$NEW_PACKAGE")

# --- Step 1: Replace package name in file contents ---
echo "Step 1: Replacing package name in file contents..."
FILES_CHANGED=0

while IFS= read -r -d '' file; do
    if "$DRY_RUN"; then
        echo "  [dry-run] would update: $file"
    else
        sed "${SED_INPLACE[@]}" "s/$OLD_ESCAPED/$NEW_PACKAGE/g" "$file"
    fi
    FILES_CHANGED=$((FILES_CHANGED + 1))
done < <(grep -rl \
    --include="*.kt" --include="*.java" --include="*.swift" \
    --include="*.xml" --include="*.plist" \
    --include="*.gradle" --include="*.gradle.kts" --include="*.toml" \
    --include="*.properties" --include="*.pro" \
    --include="*.pbxproj" --include="*.xcconfig" \
    --include="*.yaml" --include="*.yml" \
    --exclude-dir="build" --exclude-dir=".gradle" --exclude-dir=".idea" \
    --exclude-dir="scripts" \
    -F "$OLD_PACKAGE" . 2>/dev/null \
    | tr '\n' '\0' \
    || true)

echo "  $FILES_CHANGED file(s) updated."
echo ""

# --- Step 2: Move directory structure ---
echo "Step 2: Moving directory structure..."
DIRS_MOVED=0

# Collect all matching directories into a temp file before moving anything,
# to avoid modifying the tree while iterating over it. Reverse-sorted so that
# deeper paths (more slashes) are processed before shallower ones — this
# ensures a nested match is moved before its ancestor, not vice versa.
TMPFILE=$(mktemp)
trap 'rm -f "$TMPFILE"' EXIT

find . -type d -path "*/$OLD_PATH" \
    -not -path "*/build/*" \
    -not -path "*/.gradle/*" \
    -not -path "*/.idea/*" \
    -print0 \
    | tr '\0' '\n' \
    | awk '{depth=gsub("/","/",$0); print depth "\t" $0}' \
    | sort -rn -t $'\t' -k1,1 \
    | awk '{sub(/^[0-9]+\t/,"")}1' \
    | tr '\n' '\0' \
    > "$TMPFILE"

while IFS= read -r -d '' old_dir; do
    # Use suffix trimming (not first-match replacement) to avoid corrupting the
    # project path if it happens to contain the package name as a path segment.
    prefix="${old_dir%$OLD_PATH}"
    new_dir="${prefix}${NEW_PATH}"

    [ "$old_dir" != "$new_dir" ] || continue
    [ -d "$old_dir" ] || continue

    if "$DRY_RUN"; then
        echo "  [dry-run] would move: $old_dir -> $new_dir"
    else
        mkdir -p "$(dirname "$new_dir")"
        if [ -d "$new_dir" ]; then
            # Target already exists: move contents individually to merge
            while IFS= read -r -d '' item; do
                mv "$item" "$new_dir/"
            done < <(find "$old_dir" -maxdepth 1 -mindepth 1 -print0)
            rmdir "$old_dir"
        else
            mv "$old_dir" "$new_dir"
        fi
        echo "  Moved: $old_dir -> $new_dir"
    fi
    DIRS_MOVED=$((DIRS_MOVED + 1))

    # Remove empty parent directories left behind
    parent="${old_dir%/*}"
    while [ "$parent" != "." ] && [ -d "$parent" ] && [ -z "$(ls -A "$parent")" ]; do
        if "$DRY_RUN"; then
            echo "  [dry-run] would remove empty dir: $parent"
        else
            rmdir "$parent"
        fi
        parent="${parent%/*}"
    done
done < "$TMPFILE"

echo "  $DIRS_MOVED director(y/ies) moved."
echo ""

# --- Summary ---
if "$DRY_RUN"; then
    echo "Done! (dry-run — no changes were made)"
    echo ""
    echo "  Files that would be updated: $FILES_CHANGED"
    echo "  Directories that would move: $DIRS_MOVED"
else
    echo "Done!"
    echo ""
    echo "  Files updated:     $FILES_CHANGED"
    echo "  Directories moved: $DIRS_MOVED"
    echo ""
    echo "Remember to run a Gradle sync in Android Studio before continuing."
fi
