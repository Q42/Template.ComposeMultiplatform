import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.gradle.api.tasks.testing.Test

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildKonfig)
    id("app.cash.licensee")
}

kotlin {
    android {
        namespace = "nl.q42.template"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        withHostTest {}
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:ui"))
            implementation(project(":core:navigation"))
            implementation(project(":core:utils"))
            implementation(project(":core:network"))
            implementation(project(":data:main"))
            implementation(project(":domain:main"))
            implementation(project(":feature:home"))
            implementation(project(":feature:onboarding"))
            api(project(":externalConfig"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.kotlinx.datetime)
            implementation(libs.room.runtime)
            implementation(libs.datadog.logs)
            implementation(libs.datadog.rum)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.compose.uiTest)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.ui.tooling)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        getByName("androidHostTest").dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.test)
        }
    }
}

val appVersionName = providers.gradleProperty("appVersionName").orElse("1.0").get()
val appVersionCode = providers.gradleProperty("appVersionCode").orElse("1").get()


// runComposeUiTest on the Android host target requires Robolectric, which it detects by
// reading Build.FINGERPRINT. A multiplatform commonTest cannot declare the required
// @RunWith(RobolectricTestRunner::class), so the shared Compose UI tests only run on the
// iOS target, where runComposeUiTest works natively, and are excluded here.
tasks.withType<Test>().configureEach {
    if (name == "testAndroidHostTest") {
        filter.excludeTestsMatching("nl.q42.template.compose.*")
    }
}

buildkonfig {
    // BuildKonfig configuration here.
    // https://github.com/yshrsmz/BuildKonfig#gradle-configuration
    packageName = "nl.q42.template"
    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "SCHEME", "template")
        buildConfigField(FieldSpec.Type.STRING, "API_BASE_URL", "https://jsonplaceholder.typicode.com/")
        buildConfigField(FieldSpec.Type.STRING, "APP_VERSION_NAME", appVersionName)
        buildConfigField(FieldSpec.Type.INT, "APP_VERSION_CODE", appVersionCode)
        buildConfigField(FieldSpec.Type.STRING, "DATADOG_CLIENT_TOKEN", "TODO ADD CLIENT TOKEN")
        buildConfigField(FieldSpec.Type.STRING, "DATADOG_RUM_APPLICATION_ID", "TODO ADD APPLICATION ID")
        buildConfigField(FieldSpec.Type.STRING, "DATADOG_SERVICE", "cmp-template")
        buildConfigField(FieldSpec.Type.STRING, "DATADOG_SITE", "EU1")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}


tasks.register("printAppVersionMetadata") {
    group = "help"
    description = "Print Android app version metadata for CI/CD workflows"

    inputs.property("versionName", appVersionName)
    inputs.property("versionCode", appVersionCode)

    doLast {
        println("VERSION_NAME=${inputs.properties["versionName"]}")
        println("VERSION_CODE=${inputs.properties["versionCode"]}")
    }
}

dependencies {
    with(libs.room.compiler) {
        add("kspAndroid", this)
        add("kspIosArm64", this)
        add("kspIosSimulatorArm64", this)
    }
}

licensee { // A gradle task "./gradlew licensee" checks the licenses of your dependencies and fails when a disallowed license is found.
    allow("Apache-2.0")
    allow("BSD-3-Clause")
    allow("MIT")
    allowUrl("https://opensource.org/license/mit")
    allowUrl("https://developer.android.com/studio/terms.html")
}
