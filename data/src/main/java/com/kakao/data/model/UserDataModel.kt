package com.kakao.data.model

data class UserDataModel(
  val kakaoId: String,
  val name: String,
  val pen: Int,
  val createdAt: Long,
  val deletedAt: Long?
)
