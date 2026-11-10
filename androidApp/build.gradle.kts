import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "nl.q42.template.androidapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "nl.q42.template.androidapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = providers.gradleProperty("appVersionCode").orElse("0").get().toInt()
        versionName = providers.gradleProperty("appVersionName").orElse("0.0.0").get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    signingConfigs {
        // Only set up when a keystore is provided (e.g. in CI), otherwise the release build stays unsigned
        providers.environmentVariable("ANDROID_KEYSTORE_FILE").orNull?.let { keystoreFile ->
            create("release") {
                storeFile = file(keystoreFile)
                storePassword = providers.environmentVariable("ANDROID_KEYSTORE_PASSWORD").orNull
                keyAlias = providers.environmentVariable("ANDROID_KEY_ALIAS").orNull
                keyPassword = providers.environmentVariable("ANDROID_KEY_PASSWORD").orNull
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "release-proguard-rules.pro"
            )

            // Upload with ./gradlew :androidApp:appDistributionUploadRelease, see README
            firebaseAppDistribution {
                artifactType = "APK"
                groups = "beta-testers"
                releaseNotes = providers.environmentVariable("FIREBASE_RELEASE_NOTES").orElse("Local build").get()
            }
        }
    }
}

dependencies {
    implementation(project(":shared"))

    // Android-specific dependencies
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.core.splashscreen)

    // Testing
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
}




