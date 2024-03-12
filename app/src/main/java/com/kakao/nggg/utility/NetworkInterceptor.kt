package com.kakao.nggg.utility

import android.content.Context
import com.kakao.core.error.NetworkError
import com.kakao.presentation.utility.ConnectionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(
  private val connectionManager: ConnectionManager
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    if (connectionManager.currentState == ConnectionManager.LOST) {
      throw NetworkError.NetworkNotConnected
    }
    return chain.proceed(chain.request())
  }
}