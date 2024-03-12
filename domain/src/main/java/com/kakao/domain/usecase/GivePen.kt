package com.kakao.domain.usecase

import com.kakao.core.error.KakaoError
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.UserRepository
import javax.inject.Inject

class GivePen @Inject constructor(
  private val preferenceRepository: PreferenceRepository,
  private val userRepository: UserRepository
) {

  suspend operator fun invoke(amount: Int) {
    val userId = preferenceRepository.id ?: throw KakaoError.NoUser
    userRepository.updatePen(userId, amount)
  }
}