plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.10"
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.skysync"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.skysync"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // manifestPlaceholders=[MAPS_API_KEY: MAPS_API_KEY]
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Scoped API
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.7")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    //Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    val nav_version = "2.8.8"
    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:$nav_version")
    // JSON serialization library, works with the Kotlin serialization plugin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    //LiveData & Compose
    val compose_version = "1.0.0"
    implementation("androidx.compose.runtime:runtime-livedata:$compose_version")
    //Location
    implementation("com.google.android.gms:play-services-location:21.3.0")
    //DataStore
    //Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //glide
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
    //Google maps
    implementation("com.google.maps.android:maps-compose:2.14.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    //Google maps Search
    //implementation("com.google.android.libraries.places:places:4.2.0")
    val work_version = "2.10.0"
    implementation("androidx.work:work-runtime-ktx:$work_version")
    ///
    implementation ("androidx.compose.material:material:1.7.8")// Or the latest version
    ///////////////////////////////////////////////////////////////////
        // --- Unit Tests (JVM) ---
      /*  testImplementation ("junit:junit:4.13.2")                          // JUnit 4
        testImplementation ("org.hamcrest:hamcrest-library:2.2")           // Hamcrest
        testImplementation ("androidx.arch.core:core-testing:2.2.0")       // LiveData/ViewModel testing
        testImplementation ("androidx.test.ext:junit-ktx:1.1.5")           // JUnit KTX
        testImplementation ("androidx.test:core-ktx:1.5.0")                // AndroidX Test Core (Kotlin)
        testImplementation ("org.robolectric:robolectric:4.12.1")          // Robolectric (Android mocking)
        testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // Coroutines testing
        testImplementation ("io.mockk:mockk:1.13.8")                       // MockK (Kotlin mocking)
        testImplementation ("app.cash.turbine:turbine:1.0.0")              // Flow testing
        testImplementation ("com.google.truth:truth:1.1.5")                // Fluent assertions

        // --- Instrumented Tests (Android) ---
        androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")    // Espresso (UI)
        androidTestImplementation ("androidx.test.ext:junit:1.1.5")                  // AndroidX JUnit
        androidTestImplementation ("androidx.arch.core:core-testing:2.2.0")          // Architecture Components
        androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // Coroutines (Android)
        androidTestImplementation ("io.mockk:mockk-android:1.13.8")                  // MockK (Android)

        // --- Shared Dependencies ---
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")   // Coroutines (Main)
        implementation ("com.jakewharton.timber:timber:5.0.1")        */
    // Logging
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")                  // AndroidX JUnit
    testImplementation ("io.mockk:mockk:1.13.8")

}