import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "net.inferno.teethbrush"
    compileSdk = 37

    defaultConfig {
        applicationId = "net.inferno.teethbrush"

        minSdk = 26
        targetSdk = 37

        versionCode = 1
        versionName = "1.0"

        buildFeatures {
            compose = true
//            buildConfig = true
        }
    }

    flavorDimensions += "dev"

    productFlavors {
        maybeCreate("dev").apply {
            dimension = "dev"

            minSdk = 30

            versionNameSuffix =
                "-dev-${DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now())}"

//            buildConfigField("Boolean", "DEV", "true")

            proguardFile("dev-rules.pro")
        }

        maybeCreate("deploy").apply {
            dimension = "dev"

            minSdk = 32

//            buildConfigField("Boolean", "DEV", "false")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }

        maybeCreate("preRelease").apply {
            isMinifyEnabled = true

            versionNameSuffix =
                "-${DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now())}"

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        release {
            isMinifyEnabled = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}


kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
        freeCompilerArgs.addAll(
            "-Xskip-prerelease-check",
            "-opt-in=kotlin.RequiresOptIn",
        )
    }
}

dependencies {
    //region Kotlin
    implementation(libs.kotlin.std)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    //endregion

    //region AndroidX
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.preferences)
    implementation(libs.androidx.datastore.preferences)
    //endregion

    //region Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    //endregion

    //region Wear
    implementation(libs.androidx.wear)
    implementation(libs.androidx.wear.input)
    implementation(libs.androidx.wear.ongoing)

    compileOnly(libs.google.wearable)
    implementation(libs.google.play.services.wearable)

    implementation(libs.androidx.wear.tiles)
    debugImplementation(libs.androidx.wear.tiles.renderer)
    //endregion

    //region Compose
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.bundles.compose.wear)
    //endregion

    coreLibraryDesugaring(libs.desugar.jdk.libs)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.wear.compose.ui.tooling)
}