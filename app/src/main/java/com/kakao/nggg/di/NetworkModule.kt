package com.kakao.nggg.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kakao.nggg.utility.KakaoAuthInterceptor
import com.kakao.nggg.utility.KarloInterceptor
import com.kakao.nggg.utility.NetworkInterceptor
import com.kakao.presentation.utility.ConnectionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  // 카카오 API Retrofit
  @Qualifier
  @Retention(AnnotationRetention.BINARY)
  annotation class KakaoRetrofit

  // Karlo API Retrofit
  @Qualifier
  @Retention(AnnotationRetention.BINARY)
  annotation class KarloRetrofit

  @Provides
  @Singleton
  fun providesKakaoAuthInterceptor(
    kakaoAuthInterceptor: KakaoAuthInterceptor
  ): Interceptor =
    kakaoAuthInterceptor

  @Provides
  @Singleton
  fun providesKalroInterceptor(
    karloInterceptor: KarloInterceptor
  ): Interceptor =
    karloInterceptor

  @Provides
  @Singleton
  fun providesNetworkInterceptor(
    networkInterceptor: NetworkInterceptor
  ): Interceptor =
    networkInterceptor

  @Provides
  @Singleton
  @KakaoRetrofit
  fun providesKakaoRetrofit(
    jsonConverterFactory: Converter.Factory,
    @KakaoRetrofit okHttpClient: OkHttpClient
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl("https://kapi.kakao.com/")
      .addConverterFactory(jsonConverterFactory)
      .client(okHttpClient)
      .build()

  @Provides
  @Singleton
  @KakaoRetrofit
  fun providesKakaoOkHttpClient(
    interceptor: HttpLoggingInterceptor,
    kakaoAuthInterceptor: KakaoAuthInterceptor,
    networkInterceptor: NetworkInterceptor
  ): OkHttpClient =
    OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .addInterceptor(kakaoAuthInterceptor)
      .addInterceptor(networkInterceptor)
      .build()

  @Provides
  @Singleton
  @KarloRetrofit
  fun providesKarloRetrofit(
    jsonConverterFactory: Converter.Factory,
    @KarloRetrofit okHttpClient: OkHttpClient
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl("https://api.kakaobrain.com/")
      .addConverterFactory(jsonConverterFactory)
      .client(okHttpClient)
      .build()

  @Provides
  @Singleton
  @KarloRetrofit
  fun providesKarloOkHttpClient(
    interceptor: HttpLoggingInterceptor,
    karloInterceptor: KarloInterceptor,
    networkInterceptor: NetworkInterceptor
  ): OkHttpClient =
    OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .addInterceptor(karloInterceptor)
      .addInterceptor(networkInterceptor)
      .connectTimeout(20, TimeUnit.SECONDS)
      .readTimeout(20, TimeUnit.SECONDS)
      .writeTimeout(20, TimeUnit.SECONDS)
      .build()

  @Provides
  @Singleton
  fun providesLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

  @Provides
  @Singleton
  fun providesConverterFactory(
    json: Json
  ): Converter.Factory =
    json.asConverterFactory("application/json".toMediaType())

  @Provides
  @Singleton
  fun providesJsonBuilder(): Json = Json {
    isLenient = true // Json 큰따옴표 느슨하게 체크
    ignoreUnknownKeys = true // Field 값이 없는 경우 무시
    coerceInputValues = true // "null" 이 들어간경우 default Argument 값으로 대체
  }

  @Provides
  @Singleton
  fun providesConnectionManager(
    @ApplicationContext context: Context
  ): ConnectionManager =
    ConnectionManager(context)
}