package com.kakao.data.model

data class QuizDataModel(
  val id: String,
  val score: Int,
  val questions: List<QuestionDataModel>,
  val createdAt: Long
)