plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}


android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.mobile.mobiletradermtx"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding =  true
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

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(Google.material)
    implementation(AndroidX.appConstrain)
    implementation(AndroidX.recyclerView)
    implementation (General.materialDialog)
    implementation(SharePreference.sharePreference)

    testImplementation(Testing.junit4)
    testImplementation(Testing.junitAndroidExt)

    //hilt dagger
    implementation(Hilt.hilt_dagger)
    kapt(Hilt.hilt_compiler)
    implementation(Hilt.hilt_viewModel)
    kapt(Hilt.hilt_viewModel_kapt)

    kapt(Room.roomCompiler)
    implementation(Room.roomKtx)
    implementation(Room.roomRuntime)

    implementation(Retrofit.okHttp)
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttpLoggingInterceptor)
    implementation(Retrofit.okHttpLogging)

    //google fire base and services
    implementation("com.google.firebase:firebase-core:17.2.0")
    implementation ("com.google.firebase:firebase-messaging:20.1.0")
    implementation ("com.google.firebase:firebase-auth:19.2.0")
    implementation ("com.google.firebase:firebase-storage:19.1.0")
    implementation ("com.google.firebase:firebase-database:19.2.0")
    implementation ("com.google.firebase:firebase-analytics:21.0.0")

    val mapVersion = "17.0.0"
    implementation ("com.google.android.gms:play-services-location:$mapVersion")
    implementation ("com.google.android.gms:play-services-places:$mapVersion")
    implementation ("com.google.maps:google-maps-services:0.9.1")
    //google location service
    val googleService = "4.3.2"
    implementation("com.google.gms:google-services:${googleService}")

    //Notification
    implementation ("com.nex3z:notification-badge:1.0.4")

    // ViewModel
    implementation(AndroidX.lifecycle_common)
    implementation (AndroidX.lifecycle_liveData)
    implementation (AndroidX.lifecycleViewModel)
    implementation (AndroidX.life_cycle_extension)
    implementation (AndroidX.lifecycle_runtime)


    //Coroutines Component
    implementation(Coroutines.coroutines)
    implementation(Coroutines.kotlin_coroutines_android)

    //Navigation Fragment contain the viewModels()
    implementation(Navigation.navigation_fragment)
    implementation(Navigation.navigation_ui)

    //Material Dialog




}