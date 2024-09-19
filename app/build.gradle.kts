import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val secretPropertiesFile = rootProject.file("secret.properties")
val secretProps = Properties()

if (secretPropertiesFile.exists()) {
    secretProps.load(secretPropertiesFile.inputStream())
}
val ebayApiToken: String = gradleLocalProperties(rootDir).getProperty("EBAY_API_TOKEN")
val googleApiKey: String = gradleLocalProperties(rootDir).getProperty("GOOGLE_API_KEY")
val ebayAuthorization: String = gradleLocalProperties(rootDir).getProperty("EBAY_AUTHORIZATION")
val newsApiKey: String = gradleLocalProperties(rootDir).getProperty("NEWS_API_KEY")


android {
    namespace = "com.example.abschlussprojekt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.abschlussprojekt"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "GOOGLE_API_KEY_SEC", "${secretProps["GOOGLE_API_KEY_SEC"]}")
        buildConfigField("String","FCM_API_KEY_SEC","${secretProps["FCM_API_KEY_SEC"]}")
    }

    buildTypes {
        release {
            buildConfigField("String", "EBAY_API_TOKEN", ebayApiToken)
            buildConfigField("String", "GOOGLE_API_KEY", googleApiKey)
            buildConfigField("String", "EBAY_AUTHORIZATION", ebayAuthorization)
            buildConfigField("String", "NEWS_API_KEY", newsApiKey)
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "EBAY_API_TOKEN", ebayApiToken)
            buildConfigField("String", "GOOGLE_API_KEY", googleApiKey)
            buildConfigField("String", "EBAY_AUTHORIZATION", ebayAuthorization)
            buildConfigField("String", "NEWS_API_KEY", newsApiKey)
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
        dataBinding = true
        buildConfig = true
    }
}

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.play.services.maps)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.location)
    implementation(libs.places)
    implementation(libs.androidx.ui.graphics.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.work.runtime.ktx)
    // Retrofit and Moshi
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi)

    // Coil
    implementation(libs.coil)

    // HttpLoggingInterceptor
    implementation(libs.okhttp.logging.interceptor)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.google.firebase.auth)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.play.services.location)
    implementation(libs.firebase.messaging)
    implementation (libs.androidx.work.runtime.ktx.v271)

}
