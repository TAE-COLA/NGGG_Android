package com.kakao.domain.model

data class Image(
  val id: String,
  val userId: String,
  val answer: String,
  val url: String,
  val imageString: String?
)
