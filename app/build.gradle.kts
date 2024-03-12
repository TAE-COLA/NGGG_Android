import java.util.Properties

private val keystoreProperties = Properties()
rootProject.file("keystore.properties").apply {
  if (exists()) inputStream().use { keystoreProperties.load(it) }
}

private val localProperties = Properties()
rootProject.file("local.properties").apply {
  if (exists()) inputStream().use { localProperties.load(it) }
}

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.googleServices)
  alias(libs.plugins.serialiationPlugin)
  kotlin("kapt")
}

android {
  namespace = "com.kakao.nggg"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.kakao.nggg"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = libs.versions.versionCode.get().toInt()
    versionName = libs.versions.versionName.get()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }

    val kakaoApiKey = localProperties["KAKAO_API_KEY"].toString()
    buildConfigField("String", "KAKAO_API_KEY", kakaoApiKey)
    buildConfigField("String", "KAKAO_REST_API_KEY", "${localProperties["KAKAO_REST_API_KEY"]}")
    manifestPlaceholders["KAKAO_API_KEY"] = kakaoApiKey.replace("\"", "")
  }

  signingConfigs {
    create("release") {
      keyAlias = keystoreProperties.getProperty("keyAlias")
      keyPassword = keystoreProperties.getProperty("keyPassword")
      storeFile = file(keystoreProperties.getProperty("storeFile"))
      storePassword = keystoreProperties.getProperty("storePassword")
    }
  }

  buildTypes {
    getByName("debug") {
      signingConfig = signingConfigs.getByName("debug")
    }
    getByName("release") {
      signingConfig = signingConfigs.getByName("release")
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
    freeCompilerArgs += listOf(
      // Enable compose metrics
      "-P",
      "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${rootProject.file(".").absolutePath}/compose/metric",

      // Enable compose reports
      "-P",
      "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${rootProject.file(".").absolutePath}/compose/report",
    )
  }
  buildFeatures {
    compose = true
    buildConfig = true
    dataBinding = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {

  implementation(project(path = ":presentation"))
  implementation(project(path = ":remote"))
  implementation(project(path = ":local"))
  implementation(project(path = ":data"))
  implementation(project(path = ":domain"))
  implementation(project(path = ":core"))

  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.ext.junit)
  androidTestImplementation(libs.espresso.core)

  // Hilt
  implementation(libs.hilt.android)
  implementation(libs.hilt.navigation.compose)
  ksp(libs.hilt.compiler)

  // Kakao
  implementation(libs.kakao.user)
  implementation(libs.kakao.share)

  // Timber
  implementation(libs.timber)

  // Firebase
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.firestore)
  implementation(libs.firebase.storage)

  // Serialization
  implementation(libs.serialization)
  implementation(libs.retrofit.converter)
}