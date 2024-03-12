package com.kakao.nggg.di

import com.kakao.data.repository.FileRepositoryImpl
import com.kakao.data.repository.ImageRepositoryImpl
import com.kakao.data.repository.KarloRepositoryImpl
import com.kakao.data.repository.PreferenceRepositoryImpl
import com.kakao.data.repository.QuizRepositoryImpl
import com.kakao.data.repository.StorageRepositoryImpl
import com.kakao.data.repository.UserRepositoryImpl
import com.kakao.domain.repository.FileRepository
import com.kakao.domain.repository.ImageRepository
import com.kakao.domain.repository.KarloRepository
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.QuizRepository
import com.kakao.domain.repository.StorageRepository
import com.kakao.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun bindsPreferenceRepository(
    repository: PreferenceRepositoryImpl
  ): PreferenceRepository

  @Binds
  @Singleton
  abstract fun bindsImageRepository(
    repository: ImageRepositoryImpl
  ): ImageRepository

  @Binds
  @Singleton
  abstract fun bindsKarloRepository(
    repository: KarloRepositoryImpl
  ): KarloRepository

  @Binds
  @Singleton
  abstract fun bindsUserRepository(
    repository: UserRepositoryImpl
  ): UserRepository

  @Binds
  @Singleton
  abstract fun bindsStorageRepository(
    repository: StorageRepositoryImpl
  ): StorageRepository

  @Binds
  @Singleton
  abstract fun bindsFileRepository(
    repository: FileRepositoryImpl
  ): FileRepository

  @Binds
  @Singleton
  abstract fun bindsQuizRepository(
    repository: QuizRepositoryImpl
  ): QuizRepository
}