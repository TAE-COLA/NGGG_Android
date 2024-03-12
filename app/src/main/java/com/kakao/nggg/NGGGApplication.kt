package com.kakao.nggg

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NGGGApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    FirebaseApp.initializeApp(applicationContext)
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())

      val keyHash = Utility.getKeyHash(this)
      Timber.d("keyHash: $keyHash")
    }
    KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
  }
}