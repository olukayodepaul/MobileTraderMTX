plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}


android {
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName
        testInstrumentationRunner = ProjectConfig.testInstrumentationRunner
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
        jvmTarget = ProjectConfig.jvmTarget
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

    //room
    kapt(Room.roomCompiler)
    implementation(Room.roomKtx)
    implementation(Room.roomRuntime)

    //Retrofit
    implementation(Retrofit.okHttp)
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttpLoggingInterceptor)
    implementation(Retrofit.okHttpLogging)

    //Notification
    implementation(General.badge)

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

    //Google dependencies
    implementation(Google.firebaseCore)
    implementation(Google.firebaseMessage)
    implementation(Google.firebaseAuth)
    implementation(Google.firebaseStorage)
    implementation(Google.firebaseDatabase)
    implementation(Google.firebaseAnalytics)
    implementation(Google.firebaseLocation)
    implementation(Google.firebasePlaces)
    implementation(Google.firebaseServices)
    implementation(Google.servicesPlaces)

}