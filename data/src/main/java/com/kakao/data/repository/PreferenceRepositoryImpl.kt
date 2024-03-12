package com.kakao.data.repository

import com.kakao.data.source.PreferenceDataSource
import com.kakao.domain.repository.PreferenceRepository
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
  private val preferenceDataSource: PreferenceDataSource
): PreferenceRepository {

  override var kakaoToken: String?
    get() = preferenceDataSource.kakaoToken
    set(value) { preferenceDataSource.kakaoToken = value }

  override var id: String?
    get() = preferenceDataSource.id
    set(value) { preferenceDataSource.id = value }
}