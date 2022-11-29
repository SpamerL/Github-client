@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.kryak.githubclient"
    compileSdk = 32

    defaultConfig {
        applicationId = "com.kryak.githubclient"
        minSdk = 23
        targetSdk = 32
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation(libs.fragment.ktx)
    implementation(libs.activity)
    implementation(libs.viewmodel)
    implementation(libs.cicerone)
    implementation(libs.hilt)
    implementation("androidx.startup:startup-runtime:1.1.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt(libs.hilt.compiler)
    implementation(libs.coil)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}
