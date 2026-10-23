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

Workflow: `.github/workflows/android-release.yml`

- runs on `push` to `main`
- builds release APK and AAB (`:androidApp:assembleRelease` + `:androidApp:bundleRelease`), signed when the signing secrets are set
- uploads release APK/AAB as workflow artifacts
- uploads the release APK to **Firebase App Distribution** (`beta-testers` group) when the signing and Firebase secrets are set

The upload uses the [App Distribution Gradle plugin](https://firebase.google.com/docs/app-distribution/android/distribute-gradle)
(`./gradlew :androidApp:appDistributionUploadRelease`), so we don't depend on a third-party GitHub action.
See [Android release signing and Firebase App Distribution](#android-release-signing-and-firebase-app-distribution) for the one-time setup.

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

### Data sources

Repositories never talk to a storage or network API (DataStore, Room, Ktor, ...) directly; they go through data sources:

- `SomethingLocalDataSource` (in `data/*/local/`) for anything stored on the device, including DataStore preferences such as app settings (see `SampleAppSettingsLocalDataSource`).
- `SomethingRemoteDataSource` (in `data/*/remote/`) for network calls.

When a project needs an in-memory store (e.g. a cache), keep it inside the relevant `SomethingLocalDataSource` instead of adding a separate file for it.

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

### Android release signing and Firebase App Distribution

Out of the box the Android release build is unsigned and nothing is uploaded to Firebase. The release
workflow signs the build and uploads it once the secrets below are set; until then it skips those steps.

Tester groups and release notes are configured in the `firebaseAppDistribution` block of the `release`
build type in `androidApp/build.gradle.kts`. Builds go to the `beta-testers` group.

1. **Create a release keystore** (once per app, keep it safe, e.g. in 1Password; you can't update the app
   for existing users without it):

   ```bash
   keytool -genkeypair -v -keystore release.keystore -alias release -keyalg RSA -keysize 2048 -validity 10000
   ```

   Then add these **repository secrets**:

   - `ANDROID_KEYSTORE_BASE64`: the base64-encoded keystore (`base64 -i release.keystore | pbcopy`)
   - `ANDROID_KEYSTORE_PASSWORD`: the keystore password
   - `ANDROID_KEY_ALIAS`: the key alias (`release` in the example above)
   - `ANDROID_KEY_PASSWORD`: the key password

2. **Set up Firebase App Distribution**:

   - Replace `androidApp/google-services.json` with the one from your own Firebase project. The plugin
     reads the Firebase app ID from it.
   - In the [Firebase console](https://console.firebase.google.com/), open **App Distribution** for
     your Android app and click **Get started**.
   - Under **Testers & Groups**, create a group with the alias `beta-testers` and add your testers.

3. **Create a service account and its credentials file**:

   - In the [Google Cloud console](https://console.cloud.google.com/iam-admin/serviceaccounts), select
     your Firebase project and create a service account.
   - Give it the **Firebase App Distribution Admin** role.
   - Open the service account, go to **Keys**, choose **Add key** > **Create new key** > **JSON** and
     download the credentials file. Store it safely and never commit it.
   - Add a **repository secret** `FIREBASE_CREDENTIALS` containing the plain (not base64-encoded)
     contents of that JSON file.

To upload a build from your own machine, point the plugin to the credentials file and the keystore:

```bash
export GOOGLE_APPLICATION_CREDENTIALS=/absolute/path/to/firebase-credentials.json
export ANDROID_KEYSTORE_FILE=/absolute/path/to/release.keystore
export ANDROID_KEYSTORE_PASSWORD=... ANDROID_KEY_ALIAS=... ANDROID_KEY_PASSWORD=...
./gradlew :androidApp:assembleRelease :androidApp:appDistributionUploadRelease
```

## Localization

All user-facing strings of all modules live in a single file: `core/ui/src/commonMain/composeResources/values/strings.xml`.
`core/ui` generates a public `Res` class (`nl.q42.template.core.ui.resources.Res`) that every other module uses,
so translations are managed in one place. Do not add a `strings.xml` to any other module.
Add translations as `composeResources/values-<locale>/strings.xml` next to it, in `core/ui` as well.

For CMP positional placeholders are required, so make sure to add strings properly
in the string resource and add the position to the placeholder (e.g. %1$s instead of %s).

Check the link for more info: https://kotlinlang.org/docs/multiplatform/compose-multiplatform-resources-usage.html#plurals
