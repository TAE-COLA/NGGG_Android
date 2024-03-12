package com.kakao.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionUIModel(
  val imageId: String = "",
  val myAnswer: String = "",
  val isCorrect: Boolean = false
): Parcelable