# Compose Multiplatform Application

Template for creating apps on Android, iOS and desktop. Desktop was mainly added for the hot reload
feature, which greatly increases development speed.

We can remove desktop if it's proves to be more than burden, not a gain.
To use Hot-reload, modify o create a new run configuration in Android Studio for JVM with this
command line:

```
hotRunJvm --autoReload -DmainClass=MainKt --quiet
```

## Wishlist / Issues

We keep track of our wish list / issues at our [GitHub Issues](https://github.com/Q42/Template.ComposeMultiplatform/issues) page.

## Before running!

- Install Android Studio
- check your system with [KDoctor](https://github.com/Kotlin/kdoctor). Ignore Java not found
  warnings.
- install JDK 17 or higher on your machine
- add the Kotlin Multiplatform plugin to Android Studio
- in our IDE go to Setting > Advance Setting > enable "Experimental Multiplatform IDE Feature"
- If you want to refactor applicationId & all package naming use the script in `scripts/package_renaming.sh`
  - Make sure to gradle sync before continuing after this, AS tends to get confused

### Android

To run the application on android device/emulator:

- open project in Android Studio and run imported android run configuration

### iOS

To run the application on iPhone device/simulator:

- Make sure Xcode is installed

Use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
for Android Studio

- Play from Android Studio

## Architecture

Many of the architecture choices that were made are documented
here: https://github.com/Q42/Template.Android
