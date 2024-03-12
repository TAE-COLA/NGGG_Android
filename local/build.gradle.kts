plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
}

android {
  namespace = "com.kakao.local"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

  implementation(project(path = ":data"))
  implementation(project(path = ":core"))

  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.ext.junit)
  androidTestImplementation(libs.espresso.core)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)
}