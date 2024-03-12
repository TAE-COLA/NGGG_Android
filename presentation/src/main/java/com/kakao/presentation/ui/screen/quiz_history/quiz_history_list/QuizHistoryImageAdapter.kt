package com.kakao.presentation.ui.screen.quiz_history.quiz_history_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kakao.presentation.R
import com.kakao.presentation.model.ImageUIModel

class QuizHistoryImageAdapter :
  ListAdapter<QuizHistoryImageAdapter.QuizHistoryImageItem, QuizHistoryImageViewHolder>(
    QuizHistoryImageItemComparator()
  ) {

  data class QuizHistoryImageItem(val image: ImageUIModel, val isCorrect: Boolean)

  class QuizHistoryImageItemComparator : DiffUtil.ItemCallback<QuizHistoryImageItem>() {
    override fun areItemsTheSame(
      oldItem: QuizHistoryImageItem,
      newItem: QuizHistoryImageItem
    ): Boolean = oldItem === newItem

    override fun areContentsTheSame(
      oldItem: QuizHistoryImageItem,
      newItem: QuizHistoryImageItem
    ): Boolean = oldItem.image.id == newItem.image.id
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizHistoryImageViewHolder =
    QuizHistoryImageViewHolder(
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context), R.layout.item_quiz_history_image, parent, false
      )
    )

  override fun onBindViewHolder(holder: QuizHistoryImageViewHolder, position: Int) {
    holder.bind(currentList[position], position)
  }
}