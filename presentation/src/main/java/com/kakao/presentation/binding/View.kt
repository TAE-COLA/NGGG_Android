package com.kakao.presentation.binding

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.kakao.presentation.model.QuizUIModel

@BindingAdapter("loading", "loading_isLoadingIndicator", requireAll = false)
fun setVisibility(view: View, loading: Boolean, isIndicator: Boolean = false) {
  view.visibility = when {
    isIndicator -> if (loading) View.VISIBLE else View.GONE
    else -> if (loading) View.GONE else View.VISIBLE
  }
}

@BindingAdapter("layout_vertical_margin", "layout_vertical_position")
fun setVerticalPadding(view: View, marginDp: Int, position: Int) {
  val density = view.context.resources.displayMetrics.density
  if (position == 0) view.updateLayoutParams<MarginLayoutParams> { topMargin = 0 }
  else view.updateLayoutParams<MarginLayoutParams> { topMargin = (marginDp * density).toInt() }
}

@BindingAdapter("layout_horizontal_margin", "layout_horizontal_position")
fun setHorizontalPadding(view: View, marginDp: Int, position: Int) {
  val density = view.context.resources.displayMetrics.density
  if (position == 0) view.updateLayoutParams<MarginLayoutParams> { marginStart = 0 }
  else view.updateLayoutParams<MarginLayoutParams> { marginStart = (marginDp * density).toInt() }
}

@BindingAdapter("loading", "quiz_quizList")
fun setVisibility(view: View, loading: Boolean, quizList: List<QuizUIModel>) {
  view.visibility = if (!loading && quizList.isEmpty()) View.VISIBLE else View.GONE
}