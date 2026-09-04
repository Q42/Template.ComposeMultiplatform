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

// gradle/gradle-daemon-jvm.properties is a Gradle-generated file (via `updateDaemonJvm`) that
// pins the JDK running the Gradle daemon itself. It cannot read the version catalog directly
// because Gradle needs it before any build script is evaluated, so we instead verify here, on
// every build, that it hasn't drifted from the catalog's `jvmToolchain` version - which stays
// the single place a human edits. Run `./gradlew updateDaemonJvm --jvm-version=$jvmToolchainVersion`
// after changing the catalog version to bring the daemon back in sync.
val daemonJvmPropertiesFile = file("gradle/gradle-daemon-jvm.properties")
if (daemonJvmPropertiesFile.exists()) {
    val daemonToolchainVersion = java.util.Properties().apply {
        daemonJvmPropertiesFile.inputStream().use { load(it) }
    }.getProperty("toolchainVersion")

    check(daemonToolchainVersion == jvmToolchainVersion.toString()) {
        "gradle/gradle-daemon-jvm.properties toolchainVersion ($daemonToolchainVersion) does not match " +
            "the jvmToolchain version in gradle/libs.versions.toml ($jvmToolchainVersion). " +
            "Run `./gradlew updateDaemonJvm --jvm-version=$jvmToolchainVersion` to fix it."
    }
}

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
