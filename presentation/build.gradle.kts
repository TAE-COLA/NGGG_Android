import java.util.Properties

private val properties = Properties()
rootProject.file("local.properties").apply {
  if (exists()) inputStream().use { properties.load(it) }
}

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.googleServices)
  alias(libs.plugins.navigationSafeArgs)
  alias(libs.plugins.serialiationPlugin)
  kotlin("plugin.parcelize")
  kotlin("kapt")
}

android {
  namespace = "com.kakao.presentation"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")

    val kakaoApiKey = properties["KAKAO_API_KEY"].toString()
    buildConfigField("String", "KAKAO_API_KEY", kakaoApiKey)
    manifestPlaceholders["KAKAO_API_KEY"] = kakaoApiKey.replace("\"", "")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
}

dependencies {

  implementation(project(path = ":domain"))
  implementation(project(path = ":core"))

  implementation(libs.core.ktx)
  implementation(libs.activity)
  implementation(libs.activity.compose)
  implementation(libs.appcompat)
  implementation(libs.material)
  implementation(libs.constraintlayout)
  implementation(libs.legacy.support.v4)
  implementation(libs.lifecycle.livedata.ktx)

  testImplementation(libs.junit)
  androidTestImplementation(libs.ext.junit)
  androidTestImplementation(libs.espresso.core)

  val composeBom = platform(libs.compose.bom)
  implementation(composeBom)
  androidTestImplementation(composeBom)
  implementation(libs.compose.foundation)
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.graphics)
  implementation(libs.compose.ui.tooling.preview)
  implementation(libs.compose.material3)
  implementation(libs.compose.material3.window)
  androidTestImplementation(libs.compose.ui.test)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.ui.test.manisfest)

  // Hilt
  implementation(libs.hilt.android)
  implementation(libs.hilt.navigation.compose)
  ksp(libs.hilt.compiler)

  // Kakao
  implementation(libs.kakao.user)
  implementation(libs.kakao.share)

  // Lifecycle
  implementation(libs.lifecycle.compose)
  implementation(libs.lifecycle.ktx)
  implementation(libs.lifecycle.viewmodel.ktx)
  implementation(libs.lifecycle.viewmodel.compose)
  implementation(libs.lifecycle.viewmodel.savedstate)

  // Navigation
  implementation(libs.navigation.ui.ktx)
  implementation(libs.navigation.fragment.ktx)
  implementation(libs.navigation.compose)

  // Timber
  implementation(libs.timber)

  // Coil
  implementation(libs.coil)
  implementation(libs.coil.landscapist)

  // Serialization
  implementation(libs.serialization)

  // Fragment
  implementation(libs.fragment)
}