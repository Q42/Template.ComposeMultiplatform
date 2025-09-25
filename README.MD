# Compose Multiplatform Application

Template for creating apps on Android, iOS and desktop. Desktop was mainly added for the hot reload feature, which greatly increases development speed.

We can remove desktop if it's proves to be more than burden, not a gain.
To use Hot-reload, modify o create a new run configuration in Android Studio for JVM with this command line:
```
hotRunJvm --autoReload -DmainClass=MainKt --quiet
```

## Wishlist
Please add your name before a wish if before you implement it, so we don't overlap our tasks.
- Setup clean architecture module structure
- Setup an [umbrella module](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-project-configuration.html#several-shared-modules) with iOS framework for all non-UI code (everything from ViewModels and below) so the iOS app can easily be split into KMP + SwiftUI if desired
- Setup proper Dependency Injection (Koin is in the project already, but not used)
- Setup real navigation, with a few example screens, like in Template.Android app (using [Navigation3](https://developer.android.com/guide/navigation/navigation-3) if possible).
- Add an example of an iOS modal (fullscreen on Android)
- Setup real networking, with an example, like in Template.Android app.
- Setup a DB (the basics) and store something
- Setup datastore (the basics) and store something
- Don't copy over ActionResult but try and add niceties for Kotlins [new rich error type](https://xuanlocle.medium.com/kotlin-2-4-introduces-rich-errors-a-game-changer-for-error-handling-413d281e4a05).
- Add an example of a SwiftUI view in a composable
- Add an example of accessing native iOS function from kotlin code.
- Add examples for accessing different types of resources
- add Firebase & crashlytics.

## Before running!

- Install Android Studio
- check your system with [KDoctor](https://github.com/Kotlin/kdoctor). Ignore Java not found warnings.
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
