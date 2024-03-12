package com.kakao.domain.usecase

import com.kakao.domain.repository.UserRepository
import javax.inject.Inject

class InsertUser @Inject constructor(
  private val userRepository: UserRepository
) {

  suspend operator fun invoke(kakaoId: String, name: String): String =
    userRepository.insertUser(kakaoId, name)
}