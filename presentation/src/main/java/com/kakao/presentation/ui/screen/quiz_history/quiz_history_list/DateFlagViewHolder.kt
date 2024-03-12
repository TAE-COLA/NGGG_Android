package com.kakao.presentation.ui.screen.quiz_history.quiz_history_list

import com.kakao.presentation.base.BaseViewHolder
import com.kakao.presentation.databinding.ItemQuizHistoryDateFlagBinding

class DateFlagViewHolder(
  private val binding: ItemQuizHistoryDateFlagBinding
) : BaseViewHolder<QuizHistoryAdapter.QuizHistoryItem>(binding) {

  override fun bind(item: QuizHistoryAdapter.QuizHistoryItem, position: Int) {
    with(item as QuizHistoryAdapter.QuizHistoryItem.DateFlagItem) {
      binding.date = date
    }
  }
}