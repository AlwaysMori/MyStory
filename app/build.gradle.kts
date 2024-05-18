plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")

}

android {
    namespace = "com.nanda.mystory"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nanda.mystory"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")

    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
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
}

dependencies {
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)


    implementation("androidx.activity:activity-ktx:1.9.0")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")


    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")


    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.github.yalantis:ucrop:2.2.8")
    implementation("io.coil-kt:coil:2.6.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")


    implementation("androidx.camera:camera-lifecycle:1.3.3")
    implementation("androidx.camera:camera-camera2:1.3.3")
    implementation("androidx.camera:camera-view:1.3.3")


    implementation("androidx.datastore:datastore-preferences:1.1.0")
}