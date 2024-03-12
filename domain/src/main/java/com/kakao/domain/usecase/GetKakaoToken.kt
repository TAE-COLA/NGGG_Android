package com.kakao.domain.usecase

import com.kakao.domain.repository.PreferenceRepository
import javax.inject.Inject

class GetKakaoToken @Inject constructor(
  private val preference: PreferenceRepository
) {

  operator fun invoke(): String? = preference.kakaoToken
}