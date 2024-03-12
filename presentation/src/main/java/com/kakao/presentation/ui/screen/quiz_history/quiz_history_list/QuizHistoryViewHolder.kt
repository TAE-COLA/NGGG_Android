package com.kakao.presentation.ui.screen.quiz_history.quiz_history_list

import com.kakao.presentation.base.BaseViewHolder
import com.kakao.presentation.databinding.ItemQuizHistoryBinding

class QuizHistoryViewHolder(
  private val binding: ItemQuizHistoryBinding
) : BaseViewHolder<QuizHistoryAdapter.QuizHistoryItem>(binding) {

  override fun bind(item: QuizHistoryAdapter.QuizHistoryItem, position: Int) {
    with(item as QuizHistoryAdapter.QuizHistoryItem.QuizItem) {
      binding.quiz = quiz
      binding.imageList = imageList
      binding.onClickQuiz = onClickQuiz
    }
  }
}