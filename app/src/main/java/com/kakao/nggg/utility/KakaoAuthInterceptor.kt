package com.kakao.nggg.utility

import com.kakao.core.error.KakaoError
import com.kakao.domain.repository.PreferenceRepository
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class KakaoAuthInterceptor @Inject constructor(
  private val preference: PreferenceRepository
) : Interceptor {
  companion object {
    private const val AUTH = "Authorization"
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()

    val authRequest = originalRequest
      .newBuilder()
      .apply {
        preference.kakaoToken?.let { token ->
          addHeader(AUTH, token)
          Timber.tag("okhttpHeader").e("$AUTH: $token")
        } ?: throw KakaoError.NoKakaoToken
      }
      .build()
    return chain.proceed(authRequest)
  }
}