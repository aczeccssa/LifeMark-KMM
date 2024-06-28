import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// MARK: Plugin Configuration
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
    kotlin("plugin.serialization") version "2.0.0"
}

// MARK: Kotlin Compile Configuration
kotlin {
    // FIXME: Add compilerOptions for low gradle version(8.3).
    // @OptIn(ExperimentalKotlinGradlePluginApi::class)
    // compilerOptions {
    //     languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
    //     apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
    // }

    // MARK: Android Configuration
    androidTarget {
        // FIXME: Replace compilations.all... to compilerOptions.
        @OptIn(ExperimentalKotlinGradlePluginApi::class) compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    // MARK: iOS && macOS Configuration
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = false
        }
    }

    // MARK: Dependence
    sourceSets {
        androidMain.dependencies {
            // Android BOM
            implementation(project.dependencies.platform(libs.androidx.compose.bom))

            // Jetpack compose
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

            // Android context
            implementation(libs.generativeai)

            // Ktor
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.android)
            implementation(libs.android.driver)

            // Koin integration
            implementation(libs.voyager.koin)
        }
        commonMain.dependencies {
            // Jetpack compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Viewmodel support for Compose multiplatform
            implementation(libs.lifecycle.viewmodel.compose)

            // Appyx material3
            implementation(libs.utils.material3)

            // Ktor client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.client.cio)

            // Compose multiplatform blur extension
            implementation(libs.haze)
            implementation(libs.haze.materials.v072)

            // Kotlinx
            implementation(libs.runtime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            // Kotlin asynchronous media loading and caching library for Compose
            implementation(libs.kamel.image)

            // Kotlin multi-platform Dependency Injection framework
            implementation(libs.koin.core)

            // Voyager navigator
            implementation(libs.voyager.navigator) // Navigator
            implementation(libs.voyager.transitions) // Transitions

            // Markdown syntax(release: 0.7.0)
            implementation(libs.markdown)

            // Web view support for kotlin multiplatform
            api(libs.compose.webview.multiplatform)

            // UUID
            implementation(libs.benasher44.uuid)

            // File picker support for kotlin multiplatform
            implementation(libs.mpfilepicker)

            // Image picker and camera support for kotlin multiplatform mobile
            implementation(libs.peekaboo.ui)
            implementation(libs.peekaboo.image.picker)

            // Compose icons
            implementation(libs.simple.icons)
            implementation(libs.eva.icons)

            // Logger
            implementation(libs.napier)

            // Lottie animation(Third part)
            implementation(libs.compottie)

            // Koin 
            implementation(libs.koin.core)
        }
        iosMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }
    }
}

// MARK: Common resources generated configuration
compose.resources {
    publicResClass = true
    generateResClass = always
}

// MARK: Android Configuration
android {
    namespace = "com.lestere.lifemark.kotlinmultiplatformmobile"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.lestere.lifemark.kotlinmultiplatformmobile"
        minSdk = libs.versions.android.minSdk.get().toInt()
        // targetSdkVersion(libs.versions.android.targetSdk.get().toInt())
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.1.3"
    }
    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    buildTypes.getByName("release").isMinifyEnabled = false
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

/**
 * MARK: SQLDelight Configuration
 * MARK:
 *   Database name is "AppDatabase".
 *   Automatically generated database implementation name is same with it.
 *   Init sqlite file should be placed in the set package.
 *   The generated implementation is in the same package.
 */
sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.lestere.lifemark.kotlinmultiplatformmobile.cache")
        }
    }
}