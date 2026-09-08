# Kickstarter Compose Multiplatform Application

This kickstarter gives our projects a flying start and ensures everything we’ve learned from earlier work can be easily reused in new projects.

Kickstarter for creating apps on Android and iOS.

## Wishlist / Issues

We keep track of our wish list / issues at our [GitHub Issues](https://github.com/Q42/Template.ComposeMultiplatform/issues) page.

## Before running

- Install Android Studio
- check your system with [KDoctor](https://github.com/Kotlin/kdoctor). Ignore Java not found
  warnings.
- add the Kotlin Multiplatform plugin to Android Studio
- in our IDE go to Setting > Advance Setting > enable "Experimental Multiplatform IDE Feature"
- If you want to refactor applicationId & all package naming use the script in `scripts/package_renaming.sh`
  - Make sure to Gradle sync after running this script; Android Studio tends to get confused

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

- runs on `pull_request` and pushes to `main`
- validates dependency licenses (`./gradlew licensee`)
- builds Android debug APK (`:androidApp:assembleDebug`)
- runs project checks (`./gradlew check`, excluding iOS simulator tests)
- uploads debug APK as a workflow artifact

### Android CD

Workflow: `.github/workflows/android-cd-release.yml`

- runs on `push` to `develop`
- builds release APK and AAB (`:androidApp:assembleRelease` + `:androidApp:bundleRelease`)
- uploads release APK/AAB as workflow artifacts

### iOS CI

Workflow: `.github/workflows/ios-ci-tests.yml`

- runs on `pull_request` and `push`
- validates Xcode/KMP configuration (`:shared:checkXcodeProjectConfiguration`)
- runs iOS simulator Kotlin tests (`:shared:iosSimulatorArm64Test`)
- builds iOS debug framework for simulator (`:shared:linkDebugFrameworkIosSimulatorArm64`)
- builds the iOS app for simulator via `xcodebuild`

### iOS CD

Workflow: `.github/workflows/ios-cd-release.yml`

- runs on `push` to `develop`
- builds and code signs iOS app archives for all environments
- uploads debug symbols to Crashlytics
- uploads to TestFlight

Note: because the Kotlin Native framework is built using a 'run script' phase, the Xcode build setting `ENABLE_USER_SCRIPT_SANDBOXING` must remain set to `NO`.

## Architecture

Many of the architecture choices that were made are documented
here: <https://github.com/Q42/Template.Android>

When data needs to be stored on disk, create a dedicated `Entity` model for the local layer.
Do not reuse domain or network models for persistence and do not add serialization/database annotations to those models just to make storage work.

## DataStore storage and backup behavior

The main data module uses a single DataStore instance for persistent preferences (for example app settings like onboarding states, theme, or language choices).

### iOS

- Stored in `Library/Application Support/` (`NSApplicationSupportDirectory`)
- Included in standard iCloud backups to preserve user settings across devices

### Android

- Stored in `Context.filesDir` (under the `datastore/` subdirectory)
- Included in the regular Auto Backup flow by default

## Versioning

The CI release workflows use version metadata from two different sources:

- `app.versionName`: read from `gradle.properties` and used as the semantic version label in release artifact names (for example `1.0.0`)
- `github.run_number`: provided by GitHub Actions via `${{ github.run_number }}` and used as the CI-driven `version_code` in workflow outputs

To change the visible release version label in CI artifacts, update `app.versionName` in `gradle.properties`. The CI run number is supplied automatically by GitHub Actions for each workflow run.

## CI configuration

Before the iOS release workflow can succeed, review and adjust the following in `.github/workflows/ios-release.yml` for your project's specific needs:

- the `matrix.environment` entry — `app_identifier`, `firebase_plist`, `code_sign_entitlements`, and `scheme`
- the `env` block — `XCODE_VERSION` and `XCODE_PROJECT`

The following **environment variables** must be configured in the repository's GitHub settings:

- `APPLE_DEVELOPER_TEAM_ID` should be set to your developer team ID. You can find this value at: <https://developer.apple.com/account>

The following **environment secrets** must be configured in the repository's GitHub settings:

- `IOS_CERTIFICATE_BASE64` contains a base64-encoded string of the .p12 certificate bundle, used to code sign the app. This bundle should contain two certificates: **development** and **distribution**.
- `IOS_CERTIFICATE_PASSWORD` contains the password of the certificate bundle.
- `APP_STORE_CONNECT_API_KEY_BASE64` contains a base64-encoded string of the .p8 App Store Connect API key.
- `APP_STORE_CONNECT_API_KEY_ID` contains the key ID of the App Store Connect API key.
- `APP_STORE_CONNECT_API_KEY_ISSUER_ID` contains the issuer ID of the App Store Connect API key.

To create a p12 a certificate bundle, open Keychain Access. Unfold the entries for the development and distribution certificate. Select the certificates and their private keys using shift, then right-click and select "Export 4 items...".

You can encode a file to base64 on the command line like this: `base64 -i ~/Desktop/Certificates.p12 | pbcopy`. This automatically puts the result on your clipboard.
Using the same base64 command, the App Store Connect API key can be encoded.

## Localization

For CMP positional placeholders are required, so make sure to add strings properly
in the string resource and add the position to the placeholder (e.g. %1$s instead of %s).

Check the link for more info: https://kotlinlang.org/docs/multiplatform/compose-multiplatform-resources-usage.html#plurals
