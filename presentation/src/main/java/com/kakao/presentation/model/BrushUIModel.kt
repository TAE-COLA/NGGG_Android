package com.kakao.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BrushUIModel(
  val color: Long = 0xFF000000,
  val width: Float = 10f
) : Parcelable