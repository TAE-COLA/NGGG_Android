package com.kakao.domain.usecase

import com.kakao.domain.repository.UserRepository
import javax.inject.Inject

class GetUserIdByKakaoId @Inject constructor(
  private val userRepository: UserRepository
) {

  suspend operator fun invoke(kakaoId: String): String? =
    userRepository.getUserIdByKakaoId(kakaoId)
}