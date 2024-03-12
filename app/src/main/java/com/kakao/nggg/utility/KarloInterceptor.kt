package com.kakao.nggg.utility

import com.kakao.nggg.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class KarloInterceptor @Inject constructor() : Interceptor {
  companion object {
    private const val AUTH = "Authorization"
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val restApiKey = BuildConfig.KAKAO_REST_API_KEY

    val authRequest = originalRequest
      .newBuilder()
      .addHeader(AUTH, "KakaoAK $restApiKey")
      .build()
      .also {
        Timber.tag("okhttpHeader").e("$AUTH: KakaoAK $restApiKey")
      }
    return chain.proceed(authRequest)
  }
}