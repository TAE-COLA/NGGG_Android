package com.kakao.domain.model

data class Quiz(
  val id: String,
  val score: Int,
  val questions: List<Question>,
  val createdAt: Long
)