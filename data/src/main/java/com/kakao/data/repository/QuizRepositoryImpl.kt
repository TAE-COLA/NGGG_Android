package com.kakao.data.repository

import com.kakao.data.model.mapper.asData
import com.kakao.data.model.mapper.asDomain
import com.kakao.data.source.QuizDataSource
import com.kakao.domain.model.Quiz
import com.kakao.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
  private val quizDataSource: QuizDataSource
) : QuizRepository {

  override suspend fun insertQuiz(kakaoId: String, quiz: Quiz) =
    quizDataSource.insertQuiz(kakaoId, quiz.asData())

  override suspend fun getQuizHistory(kakaoId: String): List<Quiz> =
    quizDataSource.getQuizHistory(kakaoId).map { it.asDomain() }

  override suspend fun getQuizById(kakaoId: String, quizId: String): Quiz =
    quizDataSource.getQuizById(kakaoId, quizId).asDomain()
}