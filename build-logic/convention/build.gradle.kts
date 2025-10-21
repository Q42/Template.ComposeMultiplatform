plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
//        create("androidAppCompose") {
//            id = "com.eidraumain.app.convention.compose"
//            implementationClass = "com.eidraumain.convention.app.AndroidAppComposeConventionPlugin"
//        }
//
//        create("androidLibCompose") {
//            id = "com.eidraumain.lib.convention.compose"
//            implementationClass = "com.eidraumain.convention.lib.AndroidLibComposeConventionPlugin"
//        }

        create("appBuildConfig") {
            id = "nl.q42.convention.app.buildconfig"
            implementationClass = "nl.q42.template.convention.app.AppBuildConfigConventionPlugin"
        }
    }
}