#!/bin/bash

# Navigate to the project root directory (parent of scripts folder)
cd "$(dirname "$0")/.." || exit 1

read -p "Enter the current package name (e.g., nl.q42.template): " OLD_PACKAGE

if [ -z "$OLD_PACKAGE" ]; then
    echo "Error: Package name cannot be empty"
    exit 1
fi

read -p "Enter the new package name (e.g., com.example.app): " NEW_PACKAGE

if [ -z "$NEW_PACKAGE" ]; then
    echo "Error: Package name cannot be empty"
    exit 1
fi

echo "Replacing '$OLD_PACKAGE' with '$NEW_PACKAGE'..."
read -p "Continue? (y/n): " CONFIRM

if [ "$CONFIRM" != "y" ]; then
    echo "Operation cancelled"
    exit 0
fi

# Replace in file contents
echo "Replacing in file contents..."
find . -type f \
    \( -name "*.kt" -o -name "*.java" -o -name "*.xml" -o -name "*.gradle" -o -name "*.gradle.kts" -o -name "*.properties" -o -name "*.pro" \) \
    -not -path "*/build/*" \
    -not -path "*/.gradle/*" \
    -not -path "*/.idea/*" \
    -exec sed -i '' "s/nl\\.q42\\.template/${NEW_PACKAGE//./\\.}/g" {} +

# Move files and subfolders from old package path to new package path
echo "Moving files and folders from old package path to new package path..."
OLD_PATH="nl/q42/template"
NEW_PATH=$(echo "$NEW_PACKAGE" | tr '.' '/')

find . -type d -path "*/$OLD_PATH" \
    -not -path "*/build/*" \
    -not -path "*/.gradle/*" \
    -not -path "*/.idea/*" \
    | sort -r | while read -r old_dir; do
    new_dir="${old_dir/$OLD_PATH/$NEW_PATH}"
    if [ "$old_dir" != "$new_dir" ] && [ -d "$old_dir" ]; then
        mkdir -p "$new_dir"
        # Move all files and subfolders recursively
        find "$old_dir" -mindepth 1 -print0 | while IFS= read -r -d '' item; do
            rel_path="${item#$old_dir/}"
            target="$new_dir/$rel_path"
            mkdir -p "$(dirname "$target")"
            mv "$item" "$target"
            echo "Moved: $item -> $target"
        done
        # Remove the now-empty old_dir
        rmdir "$old_dir"
    fi
    # Remove empty parent directories if any
    parent_dir="$(dirname "$old_dir")"
    while [ "$parent_dir" != "." ] && [ -d "$parent_dir" ] && [ "$(ls -A "$parent_dir")" == "" ]; do
        rmdir "$parent_dir"
        parent_dir="$(dirname "$parent_dir")"
    done

done

# Final cleanup: remove any remaining empty directories
find . -type d -empty -not -path "*/build/*" -not -path "*/.gradle/*" -not -path "*/.idea/*" -delete

echo "Folder structure replacement complete!"
