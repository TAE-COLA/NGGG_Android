package com.kakao.remote.model

import com.google.firebase.firestore.PropertyName

data class ImageRemoteModel(
  @JvmField
  @PropertyName("user_id")
  val userId: String = "",
  val answer: String = "",
  val url: String = "",
  @JvmField
  @PropertyName("image_string")
  val imageString: String? = null
)