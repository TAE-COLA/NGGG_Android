// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  dependencies {
    classpath(libs.navigation.safe.args)
  }
}

plugins {
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.googleServices) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.kotlinAndroid) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.navigationSafeArgs) apply false
  alias(libs.plugins.serialiationPlugin) apply false
  alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
}