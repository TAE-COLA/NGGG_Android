package com.kakao.nggg.di

import com.kakao.remote.service.KarloService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

  @Provides
  @Singleton
  fun providesKarloService(
    @NetworkModule.KarloRetrofit retrofit: Retrofit
  ): KarloService =
    retrofit.create(KarloService::class.java)
}