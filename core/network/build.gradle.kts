plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.koin.compiler)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    android {
        namespace = "nl.q42.template.core.network"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        withHostTestBuilder {
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "core:networkKit"

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }


    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:utils"))

                implementation(libs.kotlin.stdlib)

                // Ktor
                implementation(libs.bundles.ktor.base)

                // Kotlinx
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)

                // Koin for DI
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)

                implementation(libs.kermit)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.bundles.kotlin.test)
            }
        }

        androidMain {
            dependencies {

                // OkHttp engine for Android
                implementation(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This is a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.

                // Darwin engine for iOS
                implementation(libs.ktor.client.darwin)
            }
        }
    }

}
