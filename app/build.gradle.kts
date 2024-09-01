plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.sberry_ia20"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.epsiloncob.sberryia"
        minSdk = 28
        targetSdk = 31
        versionCode = 1
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
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
    buildFeatures {
        mlModelBinding = true
    }
    buildToolsVersion = "30.0.2"
}

dependencies {
    implementation("androidx.activity:activity:1.9.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.camera:camera-view:1.3.3")
    implementation("androidx.camera:camera-lifecycle:1.3.3")
    testImplementation("junit:junit:4.13.2")
    implementation ("androidx.camera:camera-core:1.3.3")
    implementation ("androidx.camera:camera-camera2:1.3.3")
    implementation ("androidx.camera:camera-lifecycle:1.3.3")
    implementation ("androidx.camera:camera-view:1.3.3")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    // Add the dependency for the Firebase ML model downloader library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-ml-modeldownloader")
    // Also add the dependency for the TensorFlow Lite library and specify its version
    implementation("org.tensorflow:tensorflow-lite:2.13.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.4")
    //implementation("org.tensorflow:tensorflow-lite-gpu:2.9.0")

}
