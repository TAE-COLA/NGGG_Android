package com.kakao.presentation.binding

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.kakao.presentation.model.ImageQuestionUIModel
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail.QuestionHistoryAdapter

@BindingAdapter("imageQuestions", "onDataUpdated", requireAll = false)
fun setImageQuestions(view: ViewPager2, data: List<ImageQuestionUIModel>, callback: () -> Unit = {}) {
  val adapter = view.adapter as? QuestionHistoryAdapter ?: return
  adapter.submitList(data)
  callback()
}