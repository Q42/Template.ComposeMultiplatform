import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

buildscript {
    dependencies {
        classpath(libs.plugin.licensee)
    }
}

plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hotReload) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.buildKonfig) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

val jvmToolchainVersion: Int = libs.versions.jvmToolchain.get().toInt()

// Pin the JDK used to compile every Kotlin module, independently of whichever
// JDK happens to run the Gradle daemon (see gradle/gradle-daemon-jvm.properties).
subprojects {
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(jvmToolchainVersion)
        }
    }
    plugins.withId("org.jetbrains.kotlin.android") {
        extensions.configure<KotlinAndroidProjectExtension> {
            jvmToolchain(jvmToolchainVersion)
        }
    }
}
