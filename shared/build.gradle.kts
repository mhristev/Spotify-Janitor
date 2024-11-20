import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-37"
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
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
        commonMain.dependencies {
            // Ktor dependencies
            implementation(libs.ktor.client.core)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            implementation("io.ktor:ktor-client-logging:3.0.1")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.1")
            implementation("io.ktor:ktor-client-content-negotiation:3.0.1")
            // KVault dependencies for saving the token
            implementation("com.liftric:kvault:1.12.0")

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.koin.core)
            api(libs.kmp.observable.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel)

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
        iosMain {
            kotlin.srcDir("build/generated/ksp/metadata")
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

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.android)
    add("kspCommonMainMetadata", libs.room.compiler)
    add("kspAndroid", libs.room.compiler)
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
