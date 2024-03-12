package com.kakao.domain.usecase

import com.kakao.domain.repository.PreferenceRepository
import javax.inject.Inject

class CheckUserInfo @Inject constructor(
  private val preference: PreferenceRepository
) {

  operator fun invoke(): Boolean = preference.kakaoToken != null && preference.id != null
}