package com.kakao.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageUIModel(
  val id: String = "",
  val userId: String = "",
  val answer: List<String> = emptyList(),
  val url: String = "",
  val drawing: DrawingUIModel? = null
): Parcelable
