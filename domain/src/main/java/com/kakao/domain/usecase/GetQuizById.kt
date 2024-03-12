package com.kakao.domain.usecase

import com.kakao.core.error.KakaoError
import com.kakao.domain.model.Quiz
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.QuizRepository
import javax.inject.Inject

class GetQuizById @Inject constructor(
  private val quizRepository: QuizRepository,
  private val preferenceRepository: PreferenceRepository
) {

  suspend operator fun invoke(id: String): Quiz {
    val userId = preferenceRepository.id ?: throw KakaoError.NoUser
    return quizRepository.getQuizById(userId, id)
  }
}