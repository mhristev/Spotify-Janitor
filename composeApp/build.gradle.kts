import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("com.spotify.android:auth:2.1.1")
            implementation(libs.koin.androidx.compose)
            implementation("io.coil-kt.coil3:coil-compose:3.0.4")
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.4")

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)

        }
    }
}

android {
    namespace = "org.internship.kmp.martin"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.internship.kmp.martin"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["redirectSchemeName"] = "kmp-spotify-janitor"
        manifestPlaceholders["redirectHostName"] = "callback"
        manifestPlaceholders["appAuthRedirectScheme"] = "kmp-spotify-janitor"
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
    buildFeatures {
        buildConfig = true // Enable BuildConfig feature
    }

    val secretsFile = file("secrets.properties")
    if (secretsFile.exists()) {
        val secrets = Properties().apply {
            load(secretsFile.inputStream())
        }

        val clientId = secrets["CLIENT_ID"] as String
        val redirectUri = secrets["REDIRECT_URI"] as String

        buildTypes {
            getByName("debug") {
                buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
                buildConfigField("String", "REDIRECT_URI", "\"$redirectUri\"")
            }
            getByName("release") {
                buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
                buildConfigField("String", "REDIRECT_URI", "\"$redirectUri\"")
            }
        }
    } else {
        throw GradleException("Missing secrets.properties file!")
    }
}

dependencies {
    implementation(libs.androidx.browser)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.core)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    debugImplementation(compose.uiTooling)
}
