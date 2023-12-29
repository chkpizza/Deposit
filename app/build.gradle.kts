plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
}

android {
    namespace = "com.wantique.deposit"
    compileSdk = Version.compileSdk

    defaultConfig {
        applicationId = "com.wantique.deposit"
        minSdk = Version.minSdk
        targetSdk = Version.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    }
    lint{
        baseline = file("lint-baseline.xml")
        abortOnError = false
    }
}

dependencies {
    implementation(project(":auth"))
    implementation(project(":home"))
    implementation(project(":base"))
    implementation(project(":resource"))

    implementation(AndroidX.CORE)
    implementation(AndroidX.APP_COMAPT)
    implementation(Google.MATERIAL)
    implementation(AndroidX.CONSTRAINT_LAYOUT)
    testImplementation(AndroidTest.JUNIT)
    androidTestImplementation(AndroidTest.EXT_JUNIT)
    androidTestImplementation(AndroidTest.ESPRESSO_CORE)
    implementation(AndroidX.NAVIGATION_UI)
    implementation(AndroidX.NAVIGATION_FRAGMENT)
    implementation(ThirdParty.DAGGER)
    kapt(ThirdParty.DAGGER_COMPILER)
}