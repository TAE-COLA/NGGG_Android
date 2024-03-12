package com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakao.presentation.databinding.ItemQuestionCardBinding
import com.kakao.presentation.model.ImageQuestionUIModel

class QuestionHistoryAdapter() : RecyclerView.Adapter<QuestionHistoryAdapter.QuestionViewHolder>() {

  private var currentItems: List<ImageQuestionUIModel> = emptyList()

  class QuestionViewHolder(
    private val binding: ItemQuestionCardBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(question: ImageQuestionUIModel) {
      binding.item = question
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder =
    QuestionViewHolder(
      ItemQuestionCardBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
    holder.bind(currentItems[position])
  }

  override fun getItemCount(): Int = currentItems.size

  @SuppressLint("NotifyDataSetChanged")
  fun submitList(items: List<ImageQuestionUIModel>) {
    currentItems = items
    notifyDataSetChanged()
  }
}