package com.kakao.presentation.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectableWidthUIModel(
  @DrawableRes val drawableId: Int,
  val width: Float
) : Parcelable