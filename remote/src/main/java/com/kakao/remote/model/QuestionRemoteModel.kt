package com.kakao.remote.model

import com.google.firebase.firestore.PropertyName

data class QuestionRemoteModel(
  @JvmField
  @PropertyName("image_id")
  val imageId: String = "",
  @JvmField
  @PropertyName("my_answer")
  val myAnswer: String = "",
  @JvmField
  @PropertyName("is_correct")
  val isCorrect: Boolean = false
)