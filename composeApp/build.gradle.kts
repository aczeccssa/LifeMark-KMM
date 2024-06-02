// MARK: Plugin Configuration
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.sqldelight)
    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
    kotlin("plugin.serialization") version "1.9.22"
}

// MARK: Kotlin Compile Configuration
kotlin {
    // MARK: Android Configuration
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    // MARK: iOS && macOS Configuration
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
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
            implementation(libs.lifecycle.viewmodel.compose)

            // Appyx material3
            implementation(libs.utils.material3)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

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

            // WebView
            // use api since the desktop app need to access the Cef to initialize it.
            api(libs.compose.webview.multiplatform)

            // Protobuf
            implementation(libs.protolite.well.known.types)

            // UUID
            implementation(libs.benasher44.uuid)
        }
        iosMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }
    }
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
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
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