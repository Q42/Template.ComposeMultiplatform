plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "nl.q42.template.feature.home"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        withHostTestBuilder {
        }
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    jvm()

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "feature:onboardingKit"

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
                implementation(libs.kotlin.stdlib)
                implementation(libs.kermit)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.androidx.lifecycle.runtime)
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(project(":core:navigation"))
                implementation(project(":core:actionresult"))
                implementation(project(":core:ui"))
                implementation(project(":domain:main"))
                implementation(project(":data:main"))
                implementation(project(":externalConfig"))
                // Add KMP dependencies here
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                
                implementation(compose.preview)
                implementation(compose.uiTooling)
                implementation(libs.androidx.activityCompose)
                implementation(libs.androidx.emoji2)
                implementation(libs.androidx.customview)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}

compose.resources {
    packageOfResClass = "nl.q42.template.feature.home.resources"
}