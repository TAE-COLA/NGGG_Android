package com.kakao.data.source

import com.kakao.data.model.QuizDataModel

interface QuizDataSource {

  suspend fun insertQuiz(kakaoId: String, quiz: QuizDataModel)
  suspend fun getQuizHistory(kakaoId: String): List<QuizDataModel>

  suspend fun getQuizById(kakaoId: String, quizId: String): QuizDataModel
}