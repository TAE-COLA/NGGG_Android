package com.kakao.domain.model

data class User(
  val kakaoId: String,
  val name: String,
  val pen: Int,
  val createdAt: Long,
  val deletedAt: Long?
)
