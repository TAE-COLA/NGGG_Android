package com.kakao.domain.usecase

import com.kakao.core.error.KakaoError
import com.kakao.domain.model.Image
import com.kakao.domain.model.Question
import com.kakao.domain.model.Quiz
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.QuizRepository
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class InsertUserQuiz @Inject constructor(
  private val quizRepository: QuizRepository,
  private val preferenceRepository: PreferenceRepository,
) {

  suspend operator fun invoke(
    images: List<Image>,
    myAnswer: List<String>,
    score: List<Boolean?>
  ) {
    val userId = preferenceRepository.id ?: throw KakaoError.NoUser

    val questions = images.mapIndexed { index, image ->
      Question(
        imageId = image.id,
        myAnswer = myAnswer[index],
        isCorrect = score[index] ?: false
      )
    }

    val quiz = Quiz(
      id = "",
      score = score.count { it == true },
      questions = questions,
      createdAt = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().epochSecond
    )

    quizRepository.insertQuiz(userId, quiz)
  }
}
