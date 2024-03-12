package com.kakao.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class QuizUIModel(
  val id: String = "",
  val score: Int = 0,
  val questions: List<QuestionUIModel> = emptyList(),
  val createdAt: LocalDateTime = LocalDateTime.now()
): Parcelable