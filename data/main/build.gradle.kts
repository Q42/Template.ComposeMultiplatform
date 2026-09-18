plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.koin.compiler)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    android {
        namespace = "nl.q42.template.data.main"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        withHostTestBuilder {
        }
    }

    iosArm64()
    iosSimulatorArm64()


    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:actionresult"))
                implementation(project(":core:network"))
                implementation(project(":core:utils"))
                implementation(project(":domain:main"))

                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kermit)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.bundles.kotlin.test)
                implementation(libs.ktor.client.mock)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(project(":core:testing"))
            }
        }

        androidMain {
            dependencies {
            }
        }

        iosMain {
            dependencies {
            }
        }
    }

}
