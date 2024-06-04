plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.entre.gethub"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.entre.gethub"
        minSdk = 24
        targetSdk = 34
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
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://backend-gethub-kot54pmj3q-et.a.run.app/api/\"")
            buildConfigField("String", "BASE_URL_ML", "\"https://machinelearning-api-kot54pmj3q-et.a.run.app/api/\"")
        }
        debug {
            buildConfigField("String", "BASE_URL", "\"https://backend-gethub-kot54pmj3q-et.a.run.app/api/\"")
            buildConfigField("String", "BASE_URL_ML", "\"https://machinelearning-api-kot54pmj3q-et.a.run.app/api/\"")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Live Data
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // view pager
    implementation(libs.androidx.viewpager2)

    // glide
    implementation(libs.glide)
    ksp(libs.glide.ksp)


    // chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.google.zxing:core:3.4.1")

    // groupie
    implementation(libs.groupie)
    implementation(libs.groupie.viewmodel)
    implementation(libs.groupie.kotlin.android.extensions)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.logging.interceptor)
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)

    //datastore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // scan QR
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)

}