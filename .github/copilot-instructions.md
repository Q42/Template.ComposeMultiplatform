# Copilot Instructions for Template.ComposeMultiplatform

## Repository Overview

This is a **Kotlin Multiplatform (KMP) template** for building apps that run on **Android** and **iOS**. It uses **Jetpack Compose Multiplatform** as the UI framework.

The project belongs to Q42 and follows the architecture patterns documented at https://github.com/Q42/Template.Android.

---

## Project Structure

```
.
├── androidApp/          # Android application entry point
├── shared/              # Shared KMP module; wires together all modules
├── core/
│   ├── actionresult/    # Sealed result type for handling async actions
│   ├── navigation/      # Navigation destinations and shared Navigator abstraction
│   ├── network/         # Ktor HTTP client setup (platform-specific engines)
│   ├── ui/              # Shared UI components, theme, SnackbarManager, DialogPresenter
│   └── utils/           # Config models and shared utilities
├── data/
│   └── main/            # Repository implementations, Ktor API clients, Room local DB
├── domain/
│   └── main/            # Domain models, repository interfaces, use cases
├── feature/
│   ├── home/            # Home feature: screen, ViewModel, DI module
│   └── onboarding/      # Onboarding feature: screen, ViewModel, DI module
├── externalConfig/      # External configuration module (e.g. remote config)
├── gradle/
│   └── libs.versions.toml  # Version catalog – all library versions and plugin aliases
├── iosApp/              # Xcode project for iOS
├── settings.gradle.kts  # Module graph; add new modules here
└── build.gradle.kts     # Root build file with plugin aliases
```

### Package Naming Convention

All Kotlin source files use `nl.q42.template` as the root package. Sub-packages follow the module structure, e.g.:
- `nl.q42.template.feature.home.presentation`
- `nl.q42.template.data.main.remote`
- `nl.q42.template.core.navigation`

---

## Architecture

The project follows a **clean architecture** layering:

1. **`domain`** – Pure Kotlin; contains models (`data class`), repository interfaces, and use cases.
2. **`data`** – Implements repository interfaces; contains Ktor API clients (`UserApi`), DTOs, Room entities, and local data sources. Has platform-specific DI files (`.android.kt`, `.ios.kt`) for providing platform-specific Room drivers.
3. **`feature`** – Each feature is a separate Gradle module. Pattern: `Screen.kt` (Compose) → `ViewModel.kt` (AndroidX ViewModel via KMP) → use cases from domain.
4. **`shared`** – Stitches features and modules together: `createAppModules()` wires all Koin modules; `App.kt` is the root Composable; navigation graphs live under `navigation/`.

### Key Patterns

- **Dependency Injection**: [Koin](https://insert-koin.io/) with the `module { }` DSL. All modules are aggregated in `shared/src/commonMain/kotlin/.../di/createAppModules.kt`. Feature modules expose their own Koin module (e.g. `homeModule`).
- **ViewModels**: Standard `androidx.lifecycle.ViewModel` (multiplatform version). ViewModels receive dependencies via Koin constructor injection.
- **ViewState**: Sealed classes (e.g. `HomeViewState`) model Loading / Content / Error states. UI collects them via `collectAsStateWithLifecycle`.
- **Navigation**: Typed destinations defined in `core:navigation`. `Navigator` is injected into ViewModels to handle back stack operations and destination changes.
- **Error handling**: `ActionResult` (in `core:actionresult`) is a sealed result type; use the `handleAction` extension for uniform error/success handling.
- **Snackbars & Dialogs**: `SnackbarManager` and `DialogPresenter` from `core:ui` are Koin singletons injected into ViewModels.
- **Logging**: [Kermit](https://github.com/touchlab/Kermit) (`co.touchlab.kermit.Logger`); Firebase Crashlytics on Android via `CrashlyticsLogWriter`.
- **Networking**: Ktor client configured in `core:network`; platform engines are OkHttp (Android) and Darwin (iOS).
- **Local storage**: [Room KMP](https://developer.android.com/kotlin/multiplatform/room) for local database.
- **Build config**: [BuildKonfig](https://github.com/yshrsmz/BuildKonfig) generates `BuildKonfig` (e.g. `DEBUG` flag) from `buildkonfig { }` block in `shared/build.gradle.kts`.
- **Compose Resources**: Resources (strings, drawables) are in `composeResources/` inside each module's `commonMain`. Access via generated `Res` object.

---

## Build System

- **Gradle** with Kotlin DSL (`.kts` files everywhere).
- **Version Catalog**: `gradle/libs.versions.toml` is the single source of truth for all versions and library coordinates. Always use `libs.<alias>` references in `build.gradle.kts` files—never hardcode version strings.
- **Adding a new module**:
  1. Create the directory and `build.gradle.kts` following the existing pattern (see `feature/home/build.gradle.kts`).
  2. Add it to `settings.gradle.kts` with `include(":your:module")`.
  3. Reference it as `project(":your:module")` in the consuming module's dependencies.
- **KSP**: Used for Room code generation. When adding a new Room database, add the KSP dependency to all relevant targets (Android, iOS) in the `dependencies { }` block at the bottom of the consuming module's `build.gradle.kts`.

---

## Running the App

### Prerequisites
- Android Studio with Kotlin Multiplatform plugin
- JDK 17+
- Xcode (for iOS)
- Run `kdoctor` to verify your environment (ignore "Java not found" warnings)

### Android
Open the project in Android Studio and run the default Android configuration.

### iOS
Open `iosApp/iosApp.xcodeproj` in Xcode, or use the KMP plugin run configuration in Android Studio.

---

## Testing

### Running Tests

```bash
# Run all Android unit tests (skips iOS simulator tests, which need Xcode)
./gradlew check --stacktrace -x :shared:iosSimulatorArm64Test

# Run only the Android host unit tests (fastest, no emulator required)
./gradlew testAndroidHostTest
```

### Test Conventions

- **Android host tests** in `src/androidHostTest/` (or `src/androidUnitTest/` in modules without `withHostTest`) are the default unit test target because they run fast without an emulator.
- **`KoinDependencyGraphTest`** (in `shared/src/androidHostTest/`) uses Koin's `verify()` API to validate the entire DI graph at test time. **Always run this test after modifying Koin modules.** If a new type isn't covered by existing `extraTypes`, add it to the `extraTypes` list in the test.
- **Common tests** in `src/commonTest/` are for platform-agnostic logic.
- When adding use cases or new DI bindings, ensure the Koin graph test still passes.

### Dependency Licenses

```bash
./gradlew licensee
```

Dependency license validation is configured in `shared/build.gradle.kts` under the `licensee { }` block. Treat that configuration (its `allow { }` rules, including any specific license URLs) as the single source of truth for which licenses are allowed. If a new dependency fails the license check, either update the `licensee { }` allowlist explicitly (when that is acceptable for the project) or switch to a dependency that complies with the existing allowlist.

---

## CI/CD

Four workflows live under `.github/workflows/`:

- `android-tests.yml` — runs on PRs and pushes to `main`. Checks dependency licenses, assembles the debug APK, and runs unit tests using `./gradlew check` (iOS simulator tests skipped here — they run in `ios-tests.yml`).
- `android-release.yml` — triggered on `workflow_dispatch`, PRs, and pushes to `main`. Builds the release APK and AAB and uploads them as workflow artifacts. Resolves version name from `app.versionName` in `gradle.properties` and version code from `${{ github.run_number }}`.
- `ios-tests.yml` — runs on PRs and pushes to `main`. Runs project configuration checks `./gradlew :shared:checkXcodeProjectConfiguration`, unit tests `:shared:iosSimulatorArm64Test`, and runs an unsigned `xcodebuild build` against the `iosApp` scheme.
- `ios-release.yml` — triggered on `workflow_dispatch` and pushes to `main`. Archives the `iosApp` scheme, uploads to TestFlight, and uploads dSYMs to Firebase Crashlytics. Matrix is structured as a list so future consumers can add an Acceptance environment alongside Production. See README.MD for the required secrets and the `<YOUR_TEAM_ID>` placeholder in `iosApp/ExportOptions.plist`.

These workflows use **GitHub-hosted macOS runners** because the iOS jobs require Xcode. Keep any pinned `runs-on` value aligned with a currently supported GitHub Actions macOS label (for example `macos-latest` or another valid pinned macOS version used in the workflow file), rather than documenting or reintroducing a self-hosted-only setup.

Gradle is configured via the official `gradle/actions/setup-gradle@v6` action (build + configuration cache reuse across runs). The iOS workflows additionally cache `~/.konan` (Kotlin/Native toolchain) and the Swift Package Manager dependencies directory.

---

## Adding New Features

Follow this checklist when adding a new feature:

1. **Create a new feature module** under `feature/` (copy `feature/home/build.gradle.kts` as a template, update namespace/package).
2. **Add the module** to `settings.gradle.kts`.
3. **Define domain types**: add models to `domain/main`, add repository interface if needed.
4. **Implement data layer**: add API client / local data source in `data/main` if needed.
5. **Create use cases** in `domain/main`.
6. **Create ViewModel** using `androidx.lifecycle.ViewModel`; inject dependencies via Koin constructor injection.
7. **Create Screen** (`@Composable`) following the Screen → ViewModel → ViewState pattern.
8. **Create Koin module** (e.g. `featureXModule`) and include it in `createAppModules()`.
9. **Add navigation destination** to `core:navigation` and wire it into the appropriate nav graph in `shared`.
10. **Verify the Koin graph** by running `./gradlew :shared:testAndroidHostTest`.

---

## Common Pitfalls

- **Gradle configuration cache**: The project uses `org.gradle.configuration-cache=true`. Avoid using `project.afterEvaluate { }` in build scripts, as it is incompatible with the configuration cache.
- **`expect`/`actual` pattern**: Platform-specific code follows the `MyClass.kt` (expect) + `MyClass.android.kt`, `MyClass.ios.kt` (actual) pattern. See `core/network/src/` for examples.
- **iOS static frameworks**: The iOS framework (`ComposeApp`) is `isStatic = true`. Avoid adding dynamic dependencies that conflict with static linking.
- **Room on KMP**: When adding new Room entities/DAOs, remember to add KSP code generation for all three targets (`kspAndroid`, `kspIosArm64`, `kspIosSimulatorArm64`) in the module's `dependencies { }` block.
- **`NativeDependencyExample`**: Demonstrates how to inject platform-specific (non-KMP) dependencies into the shared module graph. Follow this pattern for any SDK that doesn't have a KMP artifact.
