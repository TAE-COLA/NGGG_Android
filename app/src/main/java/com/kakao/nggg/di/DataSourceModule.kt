package com.kakao.nggg.di

import com.kakao.data.source.FileDataSource
import com.kakao.data.source.ImageDataSource
import com.kakao.data.source.KarloDataSource
import com.kakao.data.source.PreferenceDataSource
import com.kakao.data.source.QuizDataSource
import com.kakao.data.source.StorageDataSource
import com.kakao.data.source.UserDataSource
import com.kakao.local.source.FileDataSourceImpl
import com.kakao.local.source.PreferenceDataSourceImpl
import com.kakao.remote.source.ImageDataSourceImpl
import com.kakao.remote.source.KarloDataSourceImpl
import com.kakao.remote.source.QuizDataSourceImpl
import com.kakao.remote.source.StorageDataSourceImpl
import com.kakao.remote.source.UserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

  @Binds
  @Singleton
  abstract fun bindsPreferenceDataSource(
    source: PreferenceDataSourceImpl
  ): PreferenceDataSource

  @Binds
  @Singleton
  abstract fun bindsImageDataSource(
    source: ImageDataSourceImpl
  ): ImageDataSource

  @Binds
  @Singleton
  abstract fun bindsKarloDataSource(
    source: KarloDataSourceImpl
  ): KarloDataSource

  @Binds
  @Singleton
  abstract fun bindsUserDataSource(
    source: UserDataSourceImpl
  ): UserDataSource

  @Binds
  @Singleton
  abstract fun bindsStorageDataSource(
    source: StorageDataSourceImpl
  ): StorageDataSource

  @Binds
  @Singleton
  abstract fun bindsFileDataSource(
    source: FileDataSourceImpl
  ): FileDataSource

  @Binds
  @Singleton
  abstract fun bindsQuizDataSource(
    source: QuizDataSourceImpl
  ): QuizDataSource
}