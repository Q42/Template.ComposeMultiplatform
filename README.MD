# Compose Multiplatform Application

Template for creating apps on Android, iOS and desktop. Desktop was mainly added for the hot reload
feature, which greatly increases development speed.

We can remove desktop if it's proves to be more than burden, not a gain.
To use Hot-reload, open the project in Android Studio and use the run configuration `🔥composeApp [jvm]`

## Wishlist / Issues

We keep track of our wish list / issues at our [GitHub Issues](https://github.com/Q42/Template.ComposeMultiplatform/issues) page.

## Before running!

- Install Android Studio
- check your system with [KDoctor](https://github.com/Kotlin/kdoctor). Ignore Java not found
  warnings.
- install JDK 17 or higher on your machine
- add the Kotlin Multiplatform plugin to Android Studio
- in our IDE go to Setting > Advance Setting > enable "Experimental Multiplatform IDE Feature"

### Android

To run the application on android device/emulator:

- open project in Android Studio and run imported android run configuration

### iOS

To run the application on iPhone device/simulator:

- Make sure Xcode is installed

Use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
for Android Studio

- Play from Android Studio

## CI/CD (GitHub Actions)

### Android CI

Workflow: `.github/workflows/android-ci-tests.yml`

- runs on `pull_request` and `push`
- validates dependency licenses (`./gradlew licensee`)
- builds Android debug APK (`:composeApp:assembleDebug`)
- runs project checks (`./gradlew check`, excluding iOS simulator tests)
- uploads debug APK as a workflow artifact

### Android CD

Workflow: `.github/workflows/android-cd-release.yml`

- runs on `push` to `develop`
- builds release APK and AAB (`:composeApp:assembleRelease` + `:composeApp:bundleRelease`)
- uploads release APK/AAB as workflow artifacts

You can download generated artifacts from the specific run in the GitHub Actions tab under **Artifacts**.

## Architecture

Many of the architecture choices that were made are documented
here: https://github.com/Q42/Template.Android
