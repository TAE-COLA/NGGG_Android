package com.kakao.presentation.binding

import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.R
import com.google.android.material.card.MaterialCardView

@BindingAdapter("isCorrectBackground")
fun setIsCorrectBackground(view: MaterialCardView, isCorrect: Boolean) {
  val attrValue = TypedValue()
  val colorResId = if (isCorrect) {
    R.attr.colorSecondaryContainer
  } else {
    R.attr.colorErrorContainer
  }
  view.context.theme.resolveAttribute(colorResId, attrValue, true)
  view.setCardBackgroundColor(ContextCompat.getColor(view.context, attrValue.resourceId))
}