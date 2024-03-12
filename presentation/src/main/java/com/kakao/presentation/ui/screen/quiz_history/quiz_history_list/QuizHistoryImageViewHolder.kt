package com.kakao.presentation.ui.screen.quiz_history.quiz_history_list

import com.kakao.presentation.base.BaseViewHolder
import com.kakao.presentation.databinding.ItemQuizHistoryImageBinding

class QuizHistoryImageViewHolder(
  private val binding: ItemQuizHistoryImageBinding
) : BaseViewHolder<QuizHistoryImageAdapter.QuizHistoryImageItem>(binding) {

  override fun bind(item: QuizHistoryImageAdapter.QuizHistoryImageItem, position: Int) {
    with(item) {
      binding.image = image
      binding.isCorrect = isCorrect
    }
    binding.position = position
  }
}