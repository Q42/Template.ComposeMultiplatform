# SKILL.md

> All code produced or left behind by this skill must follow the conventions defined in [AGENTS.MD](../../../AGENTS.MD).

## Usage

Skills are invoked via Copilot chat using natural language. Each skill also has a canonical command format for precision.

You can say things like:
- *"Remove the sample code from this template"*
- *"Strip out the sample feature so I can start my real project"*
- *"Clean up the template's example code"*

Or use the canonical format:

```
Remove the sample template code
```

---

## Skill: remove-sample-code

**Description:** Deletes everything belonging to this template's built-in example flow — everything carrying the `sample`/`Sample` prefix (see [AGENTS.MD § Template Sample Code](../../../AGENTS.MD)) — leaving the architecture scaffolding (empty `domain/*`/`data/*` Koin modules, `core/*`, DI bootstrap, navigation) intact and ready for real features.

**Run this once**, right after adopting the template for a real project. Before that point the sample code is what makes the template buildable and demoable out of the box — don't run this on a fresh checkout you still intend to use as a reference or demo.

**Input:** none. This skill always targets the entire built-in sample flow, not a partial subset.

This skill is deliberately written as a procedure, not a file list. It never hardcodes which files exist today — the template's file layout will change over time, and a hardcoded list would silently go stale. Re-discover everything at run time using the rules below.

### What counts as "sample" code

Anything named with the `sample`/`Sample` prefix (case-matching the surrounding identifier style): file/directory names, class/object names, Gradle module paths, package segments, Koin module `val`s, function parameters, etc.

A second, harder-to-spot category also counts: wiring that exists *only* to reach something sample-named, even though the wiring's own name isn't prefixed (e.g. a `Destination.Onboarding` route whose only implementing screen lives in a `feature/sample...` module). You can't grep your way to these — they surface as compiler errors in step 4 once the sample files are gone, which is exactly how this procedure is meant to find them.

### Steps (execute in order)

1. **Discover.** Run, from the repo root:
   ```
   grep -rniI "sample" --include="*.kt" --include="*.kts" --include="*.xml" --include="*.swift" --include="*.toml" . | grep -vE "/(build|\.git|\.gradle|\.idea)/"
   ```
   This is the authoritative source of truth for this run — not any list in this skill file. Present the matches to the user as a deletion/edit plan before touching anything; this is a destructive, repo-wide change.

2. **Classify each match** into one of two buckets:
   - **Whole file/directory is sample content** — the file's own name (or an ancestor directory's name) carries the `sample`/`Sample` prefix, e.g. `feature/samplehome/`, `SampleUserRepository.kt`. → delete the file or directory outright.
   - **File is real scaffolding that merely references sample content** — the file's own name has no prefix, but it imports, calls, or registers something that does (a Gradle include, a Koin `single<Sample...>()`/`factory<Sample...>()`, an import of a class from a file just deleted, a `when`/`sealed` branch, a constructor argument, etc.). → keep the file, remove only the specific line(s)/block(s) that reference the deleted content. If removing a registration leaves a DI module or Gradle dependency block empty, that's fine — leave it empty, ready for real entries.

3. **Delete everything in the first bucket** before touching the second — deleting the sample files first is what turns the second bucket's remaining references into concrete compiler errors you can work from, instead of things you have to reason about by hand.

4. **Compile and fix forward.** Build the project (`./gradlew build`, or faster iteration with `./gradlew compileDebugKotlin compileKotlinIosSimulatorArm64` plus an Xcode/SwiftPM build for `iosApp/`) and resolve every resulting error by removing the now-dangling reference in the real (non-sample) file that contains it. Repeat compile → fix until it's green. This is how you find the "second category" wiring described above (unused DI parameters, orphaned navigation routes, `when` branches with no target) without needing to enumerate it in advance.
   - If removing a reference would leave something structurally incomplete that the app actually needs to run — most notably a navigation entry point with no screen left to show (e.g. whatever `Destination` the app boots into) — don't try to reconstruct a real screen here. Insert the smallest possible placeholder (e.g. a bare `Text("TODO: replace with your first real screen")` composable) with a comment pointing at the [new-feature-module](../new-feature-module/SKILL.md) and [new-screen](../new-screen/SKILL.md) skills, so the build stays green and the next step is obvious.
   - If a DI-provided value (e.g. a constructor parameter, a Koin `single { }`) only ever existed to support sample code, remove the parameter/binding itself and update every call site — don't leave an unused parameter or a `TODO`-valued stub lying around.

5. **Re-run the discovery grep from step 1.** Anything still matching should only be incidental false positives (e.g. the English word "example" appearing near "sample" in prose, or an unrelated identifier that happens to contain the substring). Investigate and fix any real leftover; don't dismiss a match without checking it.

6. **Verify.** The build from step 4 must be green with zero remaining `sample`/`Sample`-prefixed identifiers anywhere in the repo (excluding build output directories).

## Checklist

After running this skill, verify:

- [ ] Step 1's grep, re-run fresh, returns no real matches (excluding `build/`, `.git/`, `.gradle/`, `.idea/`, and confirmed false positives)
- [ ] No file, directory, class, package, or Gradle module anywhere still carries the `sample`/`Sample` prefix
- [ ] Every DI module / Gradle dependency block that lost sample registrations still compiles (empty is fine; unused parameters are not)
- [ ] The app's navigation entry point still resolves to something renderable — a placeholder screen if no real one exists yet, never a reference to a deleted sample screen
- [ ] The build (`./gradlew build`, or equivalent per-target compiles) succeeds
- [ ] Communicated to the user: the next step is to use [new-feature-module](../new-feature-module/SKILL.md) + [new-screen](../new-screen/SKILL.md) to build the project's real first screen(s) and replace any placeholder left in step 4
