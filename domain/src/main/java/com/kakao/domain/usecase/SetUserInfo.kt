package com.kakao.domain.usecase

import com.kakao.domain.repository.PreferenceRepository
import javax.inject.Inject

class SetUserInfo @Inject constructor(
  private val preference: PreferenceRepository
) {

  operator fun invoke(
    kakaoToken: String?,
    id: String?
  ) {
    preference.kakaoToken = kakaoToken
    preference.id = id
  }
}