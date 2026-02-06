# Architecture Diagram - Post Migration

## Module Dependency Graph

```
┌─────────────────────────────────────────────────────────────────┐
│                     Template.ComposeMultiplatform                │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  androidApp (Android Application)                      │    │
│  │  ├─ Plugin: com.android.application ✅                │    │
│  │  ├─ Namespace: nl.q42.template.androidApp             │    │
│  │  ├─ Contains:                                          │    │
│  │  │  ├─ AppActivity (entry point)                      │    │
│  │  │  ├─ AndroidManifest.xml                            │    │
│  │  │  └─ Resources (icons, etc.)                        │    │
│  │  └─ Depends on: composeApp                             │    │
│  └────────────────────────────────────────────────────────┘    │
│          ↓ (implementation dependency)                         │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  composeApp (Kotlin Multiplatform Library)            │    │
│  │  ├─ Plugin: com.android.kotlin.multiplatform.library  │    │
│  │  ├─ Namespace: nl.q42.template                        │    │
│  │  ├─ Targets:                                           │    │
│  │  │  ├─ Android (via androidLibrary {})                │    │
│  │  │  ├─ JVM (Desktop)                                  │    │
│  │  │  ├─ iOS Arm64                                      │    │
│  │  │  └─ iOS Simulator Arm64                            │    │
│  │  └─ Contains:                                          │    │
│  │     ├─ commonMain (shared code)                       │    │
│  │     ├─ androidMain (platform-specific)                │    │
│  │     ├─ jvmMain (desktop-specific)                     │    │
│  │     └─ iosMain (iOS-specific)                         │    │
│  └────────────────────────────────────────────────────────┘    │
│          ↓↓↓ (depends on)                                      │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Core Modules                                          │    │
│  │  ├─ core:ui                                            │    │
│  │  ├─ core:navigation                                    │    │
│  │  ├─ core:network                                       │    │
│  │  ├─ core:utils                                         │    │
│  │  └─ core:actionresult                                  │    │
│  └────────────────────────────────────────────────────────┘    │
│          ↓ (depends on)                                        │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Feature Modules                                       │    │
│  │  ├─ feature:home                                       │    │
│  │  └─ feature:onboarding                                 │    │
│  └────────────────────────────────────────────────────────┘    │
│          ↓ (depends on)                                        │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  Data & Domain Layers                                  │    │
│  │  ├─ data:main                                          │    │
│  │  └─ domain:main                                        │    │
│  └────────────────────────────────────────────────────────┘    │
│          ↓ (depends on)                                        │
│  ┌────────────────────────────────────────────────────────┐    │
│  │  External Configuration                                │    │
│  │  └─ externalConfig                                     │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Build Configuration Structure

```
┌──────────────────────────────────────────────────────────┐
│           Root Project (build.gradle.kts)                │
│  ┌─ Plugins (apply false):                               │
│  │  ├─ org.jetbrains.kotlin.multiplatform                │
│  │  ├─ org.jetbrains.compose.compiler                    │
│  │  ├─ org.jetbrains.compose                             │
│  │  ├─ com.android.application ✅                        │
│  │  ├─ org.jetbrains.kotlin.android                      │
│  │  ├─ com.android.kotlin.multiplatform.library          │
│  │  └─ ... (other plugins)                               │
│  └─ Subprojects:                                         │
│     ├─ :composeApp                                       │
│     │  └─ Plugins:                                       │
│     │     ├─ org.jetbrains.kotlin.multiplatform ✅       │
│     │     ├─ org.jetbrains.compose.compiler ✅           │
│     │     ├─ org.jetbrains.compose ✅                    │
│     │     └─ com.android.kotlin.multiplatform.library ✅ │
│     │                                                    │
│     ├─ :androidApp ✨ NEW                                │
│     │  └─ Plugins:                                       │
│     │     ├─ com.android.application ✅                  │
│     │     └─ org.jetbrains.kotlin.plugin.compose ✅      │
│     │                                                    │
│     ├─ :core:ui                                          │
│     ├─ :core:navigation                                  │
│     ├─ :core:network                                     │
│     ├─ :core:utils                                       │
│     ├─ :core:actionresult                                │
│     ├─ :data:main                                        │
│     ├─ :domain:main                                      │
│     ├─ :feature:home                                     │
│     ├─ :feature:onboarding                               │
│     └─ :externalConfig                                   │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

---

## Source Code Structure

### Before Migration ❌
```
composeApp/
├── src/
│   ├── commonMain/
│   │   └── kotlin/...
│   ├── androidMain/
│   │   ├── AndroidManifest.xml          ← App manifest
│   │   ├── kotlin/
│   │   │   ├── App.android.kt           ← Activity
│   │   │   └── theme/Theme.android.kt
│   │   └── res/
│   │       └── mipmap-*/...             ← App icons
│   ├── jvmMain/
│   │   └── kotlin/...
│   └── iosMain/
│       └── kotlin/...
```

### After Migration ✅
```
composeApp/                          (KMP Library)
├── src/
│   ├── commonMain/
│   │   └── kotlin/...               ← Unchanged
│   ├── androidMain/
│   │   └── kotlin/
│   │       └── theme/Theme.android.kt  ← Platform impl only
│   ├── jvmMain/
│   │   └── kotlin/...               ← Unchanged
│   └── iosMain/
│       └── kotlin/...               ← Unchanged

androidApp/                          (Android App) ✨ NEW
├── src/main/
│   ├── AndroidManifest.xml          ← App manifest
│   ├── kotlin/
│   │   └── nl/q42/template/
│   │       └── AppActivity.kt       ← Activity
│   └── res/
│       └── mipmap-*/...             ← App icons
```

---

## Plugin Evolution

### Before (Deprecated ❌)
```
plugins {
    id "org.jetbrains.kotlin.multiplatform"
    id "com.android.application"          ← Not allowed with KMP
}

kotlin {
    androidTarget { ... }
}

android {
    namespace = "..."
    applicationId = "..."                 ← App config mixed with KMP
    versionCode = 1
    versionName = "1.0.0"
    // ... more app-specific config
}
```

### After (Compliant ✅)
```
// composeApp/build.gradle.kts
plugins {
    id "org.jetbrains.kotlin.multiplatform"
    id "com.android.kotlin.multiplatform.library"  ← Proper library plugin
}

kotlin {
    androidLibrary {                      ← Library configuration
        namespace = "..."
        compileSdk = ...
        minSdk = ...
    }
}

// androidApp/build.gradle.kts
plugins {
    id "com.android.application"          ← Separated from KMP
}

android {
    namespace = "..."
    applicationId = "..."                 ← App config isolated
    versionCode = 1
    versionName = "1.0.0"
    // ... app-specific config
}

dependencies {
    implementation project(":composeApp")  ← Depends on library
}
```

---

## Platform Target Mapping

```
Device Type          Platform      Source Module        Run Configuration
─────────────────────────────────────────────────────────────────────────
Android Phone        Android       composeApp + androidApp    androidApp ✅
Android Tablet       Android       composeApp + androidApp    androidApp ✅
Desktop (Mac/Win)    JVM           composeApp                 composeApp
iPad                 iOS           composeApp                 iosApp
iPhone               iOS           composeApp                 iosApp
```

---

## Gradle Task Flow

### Before (Single Module)
```
$ ./gradlew build
    ↓
composeApp:build (KMP + Android)
    ├─ commonMainClasses
    ├─ androidMainClasses
    ├─ jvmMainClasses
    ├─ iosMainClasses
    └─ assembleDebug (Android APK)  ← All in one
```

### After (Separated Modules)
```
$ ./gradlew build
    ↓
├─ composeApp:build (KMP Library)
│   ├─ commonMainClasses
│   ├─ androidMainClasses (library)
│   ├─ jvmMainClasses
│   ├─ iosMainClasses
│   └─ publishAndroidPublicationToMavenLocal
│
└─ androidApp:build (Android App)  ← Depends on composeApp
    ├─ compileDebugKotlin
    ├─ compileDebugJava
    ├─ assembleDebug (Android APK)  ← Final APK
    └─ installDebug
```

---

## Key Architectural Improvements

```
BEFORE (Mixed Concerns)              AFTER (Separated Concerns)
═════════════════════────────────────═════════════════════════

┌─────────────────────┐           ┌──────────────┐
│   composeApp        │           │  androidApp  │ (Application Layer)
│ (Everything)        │           │              │
│ ├─ KMP Code         │    →      └──────┬───────┘
│ ├─ Android App      │                  │ depends on
│ ├─ Android Config   │                  ▼
│ └─ Resources        │           ┌──────────────┐
└─────────────────────┘           │  composeApp  │ (Library Layer)
                                  │              │
                                  │ ├─ KMP Code  │
❌ Deprecation warning            │ └─ Shared    │
❌ Hard to separate               │   Impl       │
❌ Can't reuse as library         └──────────────┘
                                  
                                  ✅ AGP 9.0 compliant
                                  ✅ Clear separation
                                  ✅ Reusable library
```

---

## Migration Summary Checklist

```
Plugin Changes:
  ✅ composeApp: com.android.application → com.android.kotlin.multiplatform.library
  ✅ androidApp: (new) com.android.application
  ✅ Root: added kotlin-android plugin

Code Movement:
  ✅ AndroidManifest.xml: moved to androidApp
  ✅ AppActivity: moved to androidApp
  ✅ Resources (mipmap): moved to androidApp
  ✅ Theme.android.kt: kept in composeApp (platform impl)

Configuration:
  ✅ settings.gradle.kts: added :androidApp
  ✅ build.gradle.kts: updated plugins
  ✅ gradle/libs.versions.toml: added kotlin-android

Build:
  ✅ ./gradlew clean build: SUCCESS
  ✅ ./gradlew composeApp:build: SUCCESS
  ✅ ./gradlew androidApp:build: SUCCESS
  ✅ No deprecation warnings: RESOLVED ✨
```

---

**Architecture is now modern, compliant, and future-proof! 🎉**

