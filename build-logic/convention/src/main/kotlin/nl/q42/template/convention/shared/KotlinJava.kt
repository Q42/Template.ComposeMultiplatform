import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

internal fun configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 23
        }

        buildFeatures {
            buildConfig = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }
}