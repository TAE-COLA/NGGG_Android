package com.kakao.domain.usecase

import com.kakao.core.ImageConfig.DRAWING_BY_AI_NAME
import com.kakao.core.QuizConfig
import com.kakao.core.error.KakaoError
import com.kakao.domain.model.GeneratedImage
import com.kakao.domain.model.Image
import com.kakao.domain.model.Question
import com.kakao.domain.model.Quiz
import com.kakao.domain.repository.ImageRepository
import com.kakao.domain.repository.KarloRepository
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.QuizRepository
import com.kakao.domain.repository.StorageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

class InsertAIQuiz @Inject constructor(
  private val quizRepository: QuizRepository,
  private val imageRepository: ImageRepository,
  private val karloRepository: KarloRepository,
  private val storageRepository: StorageRepository,
  private val preferenceRepository: PreferenceRepository,
  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

  suspend operator fun invoke(
    generatedImages: List<GeneratedImage>,
    myAnswer: List<String>,
    score: List<Boolean?>
  ) {
    val userId = preferenceRepository.id ?: throw KakaoError.NoUser
    val questions = Array(QuizConfig.ROUNDS) { Question("", "", false) }
    val createdAt = LocalDateTime.now()

    CoroutineScope(ioDispatcher).launch {
      repeat(QuizConfig.ROUNDS) { index ->
        launch {
          val uniqueId = UUID.randomUUID()
          val path = "images/${createdAt.format(DateTimeFormatter.ISO_DATE)}/$uniqueId.png"
          val image = generatedImages[index]

          val imageData = karloRepository.downloadImageFile(image.url)
          val imageUrl = storageRepository.uploadFile(path, imageData)

          val id = Image(
            id = "",
            userId = DRAWING_BY_AI_NAME,
            answer = image.answer,
            url = imageUrl,
            imageString = null
          ).let {
            imageRepository.insertImage(it).id
          }

          val question = Question(
            imageId = id,
            myAnswer = myAnswer[index],
            isCorrect = score[index] ?: false
          )
          questions[index] = question
        }
      }
    }.join()

    val quiz = Quiz(
      id = "",
      score = score.count { it == true },
      questions = questions.toList(),
      createdAt = createdAt.atZone(ZoneId.systemDefault()).toInstant().epochSecond
    )

    quizRepository.insertQuiz(userId, quiz)
  }
}
