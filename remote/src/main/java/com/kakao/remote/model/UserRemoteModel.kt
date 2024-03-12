package com.kakao.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.kakao.core.Policy

data class UserRemoteModel(
  @JvmField
  @PropertyName("kakao_id")
  val kakaoId: String = "",
  val name: String = "",
  val pen: Int = Policy.DEFAULT_PEN,
  @JvmField
  @PropertyName("created_at")
  val createdAt: Timestamp = Timestamp.now(),
  @JvmField
  @PropertyName("deleted_at")
  val deletedAt: Timestamp? = null
)