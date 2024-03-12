package com.kakao.domain.usecase

import com.kakao.core.error.KakaoError
import com.kakao.domain.model.Quiz
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.QuizRepository
import javax.inject.Inject

class GetQuizHistory @Inject constructor(
  private val preferenceRepository: PreferenceRepository,
  private val quizRepository: QuizRepository
) {

  suspend operator fun invoke(): List<Quiz> {
    val userId = preferenceRepository.id ?: throw KakaoError.NoUser
    return quizRepository.getQuizHistory(userId)
  }
}