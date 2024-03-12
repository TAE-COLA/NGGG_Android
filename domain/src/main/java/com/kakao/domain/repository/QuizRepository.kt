package com.kakao.domain.repository

import com.kakao.domain.model.Quiz

interface QuizRepository {

  suspend fun insertQuiz(kakaoId: String, quiz: Quiz)
  suspend fun getQuizHistory(kakaoId: String): List<Quiz>

  suspend fun getQuizById(kakaoId: String, quizId: String): Quiz
}