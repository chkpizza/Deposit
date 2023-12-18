import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.wantique.auth"
    compileSdk = Version.compileSdk

    defaultConfig {
        minSdk = Version.minSdk

        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", getGoogleWebClientId("GOOGLE_WEB_CLIENT_ID"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        dataBinding = true
        buildConfig = true
    }
}

fun getGoogleWebClientId(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {
    implementation(project(":base"))

    implementation(AndroidX.CORE)
    implementation(AndroidX.APP_COMAPT)
    implementation(Google.MATERIAL)
    testImplementation(AndroidTest.JUNIT)
    androidTestImplementation(AndroidTest.EXT_JUNIT)
    androidTestImplementation(AndroidTest.ESPRESSO_CORE)
    implementation(AndroidX.NAVIGATION_UI)
    implementation(AndroidX.NAVIGATION_FRAGMENT)
    implementation(ThirdParty.DAGGER)
    kapt(ThirdParty.DAGGER_COMPILER)

    implementation(platform(Firebase.FIREBASE_BOM))
    implementation(Firebase.FIREBASE_ANALYTICS)
    implementation(Firebase.FIREBASE_AUTH)
    implementation(Firebase.FIRESTORE)
    implementation(Google.PLAY_SERVICE_AUTH)
}