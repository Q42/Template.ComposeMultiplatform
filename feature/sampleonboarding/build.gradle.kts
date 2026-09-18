plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.koin.compiler)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    android {
        namespace = "nl.q42.template.feature.sampleonboarding"
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
                implementation(libs.kotlin.stdlib)
                implementation(libs.kermit)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.annotations)
                implementation(libs.androidx.lifecycle.runtime)

                implementation(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)

                implementation(project(":core:navigation"))
                implementation(project(":core:actionresult"))
                implementation(project(":core:ui"))
                implementation(project(":domain:main"))
                implementation(project(":data:main"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.bundles.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.activityCompose)
                implementation(libs.androidx.emoji2)
                implementation(libs.androidx.customview)
                implementation(libs.androidx.ui.tooling)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }

}
