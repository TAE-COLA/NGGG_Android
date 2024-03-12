package com.kakao.domain.usecase

import com.kakao.core.error.KakaoError
import com.kakao.domain.model.User
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.UserRepository
import javax.inject.Inject

class GetUser @Inject constructor(
  private val preferenceRepository: PreferenceRepository,
  private val userRepository: UserRepository
) {

  suspend operator fun invoke(id: String? = null): User {
    val userId = id ?: preferenceRepository.id ?: throw KakaoError.NoUser
    return userRepository.getUserById(userId)
  }
}