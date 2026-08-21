# SKILL.md

> All code produced by this skill must follow the conventions defined in [AGENTS.MD](../../../AGENTS.MD).

## Usage

Skills are invoked via Copilot chat using natural language. Each skill also has a canonical command format for precision.

You can say things like:
- *"Create a new feature module called settings"*
- *"Add a new feature module for profile"*
- *"Scaffold a new feature module named payments"*

Or use the canonical format:

```
Create a new feature module called <Name>
```

**Example:**

> Create a new feature module called settings

Produces a new Gradle module at `feature/<name>/` with:

```
feature/settings/build.gradle.kts
feature/settings/src/androidMain/AndroidManifest.xml
feature/settings/src/commonMain/kotlin/nl/q42/template/feature/settings/di/SettingsModule.kt
```

And updates these existing files:

```
settings.gradle.kts                                              ← include(":feature:<name>")
shared/build.gradle.kts                                          ← implementation(project(":feature:<name>"))
shared/src/commonMain/kotlin/nl/q42/template/di/createAppModules.kt ← includes(<name>Module)
```

---

## Skill: create-feature-module

**Description:** Scaffolds a new feature module with Gradle configuration, empty source sets, and a Koin DI module, then wires it into the project.

**Input:** Feature name in lowercase (e.g. `settings`, `profile`, `payments`).

**Naming Conventions:**

| Concept | Convention | Example (`settings`) |
|---|---|---|
| Module path | `feature/<name>` | `feature/settings` |
| Package | `nl.q42.template.feature.<name>` | `nl.q42.template.feature.settings` |
| Android namespace | `nl.q42.template.feature.<name>` | `nl.q42.template.feature.settings` |
| Gradle include | `:feature:<name>` | `:feature:settings` |
| Koin module val | `<name>Module` | `settingsModule` |

Use `<name>` for lowercase, `<Name>` for PascalCase throughout.

---

**Steps** (execute in order):

### 1. `feature/<name>/build.gradle.kts` — Gradle module configuration

```kotlin
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    android {
        namespace = "nl.q42.template.feature.<name>"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        withHostTestBuilder {
        }
    }

    jvm()

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "feature:<name>Kit"

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kermit)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.androidx.lifecycle.runtime)
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(project(":core:navigation"))
                implementation(project(":core:actionresult"))
                implementation(project(":core:ui"))
                implementation(project(":domain:main"))
                implementation(project(":data:main"))
                // Add KMP dependencies here
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
            }
        }

        iosMain {
            dependencies {
            }
        }
    }

}
```

- Use the exact same plugin set and dependency structure as existing feature modules.
- Set `namespace` to `nl.q42.template.feature.<name>`.
- Set `xcfName` to `feature:<name>Kit`.

---

### 2. `feature/<name>/src/androidMain/AndroidManifest.xml` — Android manifest

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

</manifest>
```

---

### 3. `<Name>Module.kt` — Koin DI module in `feature/<name>/src/commonMain/kotlin/nl/q42/template/feature/<name>/di/`

```kotlin
package nl.q42.template.feature.<name>.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val <name>Module = module {
    // Register ViewModels and dependencies here
}
```

- Use `viewModelOf()` when adding ViewModels — never `single` or `factory` for ViewModels.

---

### 4. Add module to `settings.gradle.kts`

Add the new module include alongside the other feature includes:

```kotlin
include(":feature:<name>")
```

---

### 5. Add dependency in `shared/build.gradle.kts`

Add the new feature module as a dependency in the `commonMain.dependencies` block:

```kotlin
implementation(project(":feature:<name>"))
```

- Place it alongside the other `feature` project dependencies.

---

### 6. Register Koin module in `createAppModules.kt`

Add the feature's Koin module in `shared/src/commonMain/kotlin/nl/q42/template/di/createAppModules.kt`:

```kotlin
import nl.q42.template.feature.<name>.di.<name>Module

// inside createAppModules:
includes(<name>Module)
```

- Place the `includes(...)` call alongside the other feature module includes.

---

## Checklist

After running this skill, verify:

- [ ] `feature/<name>/build.gradle.kts` exists with correct namespace and dependencies
- [ ] `feature/<name>/src/androidMain/AndroidManifest.xml` exists
- [ ] `feature/<name>/src/commonMain/kotlin/nl/q42/template/feature/<name>/di/<Name>Module.kt` exists
- [ ] `settings.gradle.kts` includes `:feature:<name>`
- [ ] `shared/build.gradle.kts` has `implementation(project(":feature:<name>"))`
- [ ] `createAppModules.kt` includes `<name>Module`
- [ ] Project syncs successfully

> **Tip:** Use the [new-screen skill](../new-screen/SKILL.md) to add screens, ViewModels, and navigation to this module after creation.
