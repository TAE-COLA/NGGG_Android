package com.kakao.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeneratedImageUIModel(
  val answer: List<KeywordUIModel> = emptyList(),
  val url: String = ""
): Parcelable