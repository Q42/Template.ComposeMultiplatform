plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.datadog.android.gradle.plugin)
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

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "release-proguard-rules.pro"
            )
        }
    }
}

datadog {
    // Explicit values, matching shared/build.gradle.kts's BuildKonfig.DATADOG_SERVICE exactly —
    // without these, the plugin defaults serviceName to the applicationId ("nl.q42.template.androidapp")
    // and site to US1, which would upload mapping files under a service/site RUM events never use.
    serviceName = providers.gradleProperty("datadogServiceName").get()
    site = providers.gradleProperty("datadogSite").get()
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




