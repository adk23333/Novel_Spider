plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
}

android {
    namespace = "sanliy.spider.novel"
    compileSdk = 34

    defaultConfig {
        applicationId = android.namespace
        minSdk = 28
        targetSdk = 34
        versionCode = 3
        versionName = "2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            ndk {
                //noinspection ChromeOsAbiSupport
                abiFilters += listOf("arm64-v8a")
            }
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}", "META-INF/DEPENDENCIES")
        }
    }
}

val uiVersion = "1.6.7"
val roomVersion = "2.6.1"

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui:${uiVersion}")
    implementation("androidx.compose.ui:ui-graphics:${uiVersion}")
    implementation("androidx.compose.ui:ui-tooling-preview:${uiVersion}")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.google.accompanist:accompanist-flowlayout:0.34.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${uiVersion}")
    debugImplementation("androidx.compose.ui:ui-tooling:${uiVersion}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${uiVersion}")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")

    implementation("androidx.room:room-runtime:${roomVersion}")
    ksp("androidx.room:room-compiler:${roomVersion}")
    implementation("androidx.room:room-ktx:${roomVersion}")
    annotationProcessor("androidx.room:room-compiler:${roomVersion}")
    testImplementation("androidx.room:room-testing:${roomVersion}")

    implementation("io.github.evanrupert:excelkt:1.0.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}