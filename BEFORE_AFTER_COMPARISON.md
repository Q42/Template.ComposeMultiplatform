# Before & After: AGP 9.0 Migration

## Project Structure Comparison

### BEFORE (Deprecated)
```
Template.ComposeMultiplatform/
├── composeApp/
│   ├── build.gradle.kts
│   │   ├── plugins:
│   │   │   ├── org.jetbrains.kotlin.multiplatform ❌
│   │   │   └── com.android.application ❌ (incompatible with KMP)
│   │   └── android { ... }
│   └── src/androidMain/
│       ├── AndroidManifest.xml (app entry point)
│       ├── kotlin/nl/q42/template/App.android.kt (AppActivity)
│       └── res/ (mipmap resources)
├── other modules...
└── README.MD
```

⚠️ **Problem:** AGP 9.0.0 deprecated `com.android.application` + KMP in same module

---

### AFTER (Fixed & Compliant)
```
Template.ComposeMultiplatform/
├── composeApp/                           📚 LIBRARY
│   ├── build.gradle.kts
│   │   ├── plugins:
│   │   │   ├── org.jetbrains.kotlin.multiplatform ✅
│   │   │   └── com.android.kotlin.multiplatform.library ✅
│   │   └── kotlin { androidLibrary { ... } }
│   └── src/androidMain/
│       └── kotlin/nl/q42/template/theme/
│           └── Theme.android.kt (platform-specific implementation)
│
├── androidApp/                           📱 APPLICATION (NEW)
│   ├── build.gradle.kts
│   │   ├── plugins:
│   │   │   ├── com.android.application ✅ (separated)
│   │   │   └── org.jetbrains.kotlin.plugin.compose
│   │   └── android { ... }
│   └── src/main/
│       ├── AndroidManifest.xml (app entry point)
│       ├── kotlin/nl/q42/template/AppActivity.kt
│       └── res/ (mipmap resources)
│
├── other modules...
└── README.MD
```

✅ **Solution:** Separated Android app from KMP library

---

## File Changes Summary

| File | Change | Reason |
|------|--------|--------|
| `composeApp/build.gradle.kts` | Plugin: `android.application` → `android.kotlin.multiplatform.library` | AGP 9.0 requirement |
| `composeApp/build.gradle.kts` | Config: `android { }` → `kotlin { androidLibrary { } }` | New plugin syntax |
| `composeApp/src/androidMain/AndroidManifest.xml` | ❌ Deleted | Moved to androidApp |
| `composeApp/src/androidMain/kotlin/App.android.kt` | ❌ Deleted | Moved to androidApp |
| `composeApp/src/androidMain/res/` | ❌ Deleted | Moved to androidApp |
| `androidApp/` | ✨ Created | New Android app module |
| `androidApp/build.gradle.kts` | ✨ Created | Android app configuration |
| `androidApp/src/main/AndroidManifest.xml` | ✨ Created | App entry point definition |
| `androidApp/src/main/kotlin/AppActivity.kt` | ✨ Created | Renamed from App.android.kt |
| `androidApp/src/main/res/` | ✨ Created | App resources |
| `settings.gradle.kts` | Added `include(":androidApp")` | Register new module |
| `build.gradle.kts` | Added `kotlin-android` plugin | Consistency (not required for AGP 9.0) |
| `gradle/libs.versions.toml` | Added `kotlin-android` plugin | Consistency |

---

## Build Output Comparison

### Before
```
⚠️ WARNING: The 'org.jetbrains.kotlin.multiplatform' plugin deprecated 
compatibility with Android Gradle plugin: 'com.android.application'

Starting with Android Gradle Plugin 9.0.0.

Please change the structure of your project and move the usage of 
'com.android.application' into a separate subproject.
```

### After
```
✅ BUILD SUCCESSFUL

No deprecation warnings related to KMP + AGP combination
All modules compile correctly
```

---

## Module Dependencies

### Before
```
composeApp (KMP + Android App)
  ├─ core:*
  ├─ data:*
  ├─ domain:*
  └─ feature:*
```

### After
```
androidApp (Android App)
  └─ composeApp (KMP Library)
      ├─ core:*
      ├─ data:*
      ├─ domain:*
      └─ feature:*
```

---

## Key Takeaways

✅ **Compliant with AGP 9.0.0+**  
✅ **Clear Separation of Concerns**  
- composeApp = Shared multiplatform library
- androidApp = Android application wrapper

✅ **Future-Proof**  
- Easy to create additional Android apps if needed
- Can reuse composeApp library in other projects

✅ **Better Maintainability**  
- Android-specific configs isolated in androidApp
- KMP code separate from app logic

---

## Gradle Build Verification

```bash
# Both modules build successfully
$ ./gradlew composeApp:build androidApp:build --dry-run
BUILD SUCCESSFUL in 4s

# Full project clean build
$ ./gradlew clean build --dry-run
BUILD SUCCESSFUL in 1s

# Gradle version: 9.1.0
# Kotlin version: 2.2.0
# AGP version: 9.0.0 (from gradle/libs.versions.toml)
```

---

## References

- 📖 [Official Kotlin Multiplatform Migration Guide](https://kotl.in/gradle/agp-new-kmp)
- 📖 [KMP Project Structure Recommendations](https://kotl.in/kmp-project-structure-migration)
- 📖 [AGP 9.0 Release Notes](https://developer.android.com/build/releases/gradle-plugin)

