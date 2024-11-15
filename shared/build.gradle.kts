import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "2.0.21"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            // Ktor dependencies
            implementation(libs.ktor.client.core)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

            // KVault dependencies for saving the token
            implementation("com.liftric:kvault:1.12.0")
        }
        androidMain.dependencies {
            // Ktor dependencies
            implementation(libs.ktor.client.okhttp)

        }
        iosMain.dependencies {
            // Ktor dependencies
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            // Test dependencies
            implementation(libs.ktor.client.core)
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "org.internship.kmp.martin.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
