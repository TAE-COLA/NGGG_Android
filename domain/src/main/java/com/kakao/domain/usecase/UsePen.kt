package com.kakao.domain.usecase

import com.kakao.core.error.FireStoreError
import com.kakao.core.error.KakaoError
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.UserRepository
import javax.inject.Inject

class UsePen @Inject constructor(
  private val preferenceRepository: PreferenceRepository,
  private val userRepository: UserRepository
) {

  suspend operator fun invoke(amount: Int) {
    val userId = preferenceRepository.id ?: throw KakaoError.NoUser
    val remainPenCount = userRepository.getUserById(userId).pen
    if (remainPenCount < amount) throw FireStoreError.NotEnoughPen

    userRepository.updatePen(userId, -amount)
  }
}

