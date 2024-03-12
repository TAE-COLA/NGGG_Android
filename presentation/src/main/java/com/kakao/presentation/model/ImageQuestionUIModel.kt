package com.kakao.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageQuestionUIModel(
  val question: QuestionUIModel = QuestionUIModel(),
  val image: ImageUIModel = ImageUIModel()
): Parcelable