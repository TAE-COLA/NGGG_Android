package com.kakao.presentation.ui.screen.quiz_history.quiz_history_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakao.core.error.ViewError
import com.kakao.presentation.R
import com.kakao.presentation.databinding.ItemQuizHistoryBinding
import com.kakao.presentation.databinding.ItemQuizHistoryDateFlagBinding
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.model.QuizUIModel
import java.time.format.DateTimeFormatter

class QuizHistoryAdapter :
  ListAdapter<QuizHistoryAdapter.QuizHistoryItem, RecyclerView.ViewHolder>(QuizHistoryItemComparator()) {

  companion object {
    const val TYPE_FLAG = 0
    const val TYPE_QUIZ = 1
  }

  sealed interface QuizHistoryItem {
    data class DateFlagItem(val date: String) : QuizHistoryItem
    data class QuizItem(val quiz: QuizUIModel, val imageList: List<ImageUIModel>, val onClickQuiz: () -> Unit) : QuizHistoryItem
  }

  class QuizHistoryItemComparator : DiffUtil.ItemCallback<QuizHistoryItem>() {
    override fun areItemsTheSame(
      oldItem: QuizHistoryItem,
      newItem: QuizHistoryItem
    ): Boolean = oldItem === newItem

    override fun areContentsTheSame(
      oldItem: QuizHistoryItem,
      newItem: QuizHistoryItem
    ): Boolean = when {
      oldItem is QuizHistoryItem.DateFlagItem && newItem is QuizHistoryItem.DateFlagItem -> oldItem.date == newItem.date
      oldItem is QuizHistoryItem.QuizItem && newItem is QuizHistoryItem.QuizItem -> oldItem.quiz.id == newItem.quiz.id
      else -> false
    }
  }

  override fun getItemViewType(position: Int): Int = when (currentList[position]) {
    is QuizHistoryItem.DateFlagItem -> TYPE_FLAG
    is QuizHistoryItem.QuizItem -> TYPE_QUIZ
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
    when (viewType) {
      TYPE_FLAG -> DateFlagViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context), R.layout.item_quiz_history_date_flag, parent, false
        )
      )

      TYPE_QUIZ -> QuizHistoryViewHolder(
        DataBindingUtil.inflate<ItemQuizHistoryBinding?>(
          LayoutInflater.from(parent.context), R.layout.item_quiz_history, parent, false
        ).apply {
          rvQuizImageList.adapter = QuizHistoryImageAdapter()
        }
      )

      else -> throw ViewError.InvalidViewHolder
    }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is QuizHistoryViewHolder -> holder.bind(currentList[position], position)
      is DateFlagViewHolder -> holder.bind(currentList[position], position)
    }
  }

  fun isFlagItem(position: Int): Boolean = getItemViewType(position) == TYPE_FLAG

  fun getHeaderLayoutView(parent: RecyclerView, position: Int): View {
    val binding = DataBindingUtil.inflate<ItemQuizHistoryDateFlagBinding>(
      LayoutInflater.from(parent.context), R.layout.item_quiz_history_date_flag, parent, false
    )

    val item = currentList[position]
    binding.tvDate.text = when (item) {
      is QuizHistoryItem.DateFlagItem -> item.date
      is QuizHistoryItem.QuizItem -> item.quiz.createdAt.format(DateTimeFormatter.ofPattern("yyyy. M. d. (E)"))
    }

    return binding.root
  }
}