package com.kakao.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class QuizRemoteModel(
  val score: Int = 0,
  val questions: List<QuestionRemoteModel> = emptyList(),
  @JvmField
  @PropertyName("created_at")
  val createdAt: Timestamp = Timestamp.now()
)