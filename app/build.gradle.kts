plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.jminnovatech.mprint"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.jminnovatech.mprint"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.services.location)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

// 🔥 VIEWMODEL
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

// 🌐 RETROFIT (API)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// 🔥 OKHTTP (LOGGING + NETWORK DEBUG)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// ⚡ COROUTINES
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

// 🎯 MATERIAL ICONS (BEST UI)
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

// 🎨 IMAGE LOADING (LOGO / PROFILE)
    implementation("io.coil-kt:coil-compose:2.6.0")

// ✨ ANIMATION (Compose animation)
    implementation("androidx.compose.animation:animation:1.6.8")

// 🎬 LOTTIE (PREMIUM LOADER)
    implementation("com.airbnb.android:lottie-compose:6.4.0")

// 📱 SYSTEM UI CONTROL (STATUS BAR COLOR)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

// 🔄 NAVIGATION ANIMATION (smooth transition)
    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")

// 📦 JSON (fallback)
    implementation("com.google.code.gson:gson:2.11.0")

// 🧱 FOUNDATION EXTRA (advanced UI)
    implementation("androidx.compose.foundation:foundation:1.6.8")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.34.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:1.3.0")
}