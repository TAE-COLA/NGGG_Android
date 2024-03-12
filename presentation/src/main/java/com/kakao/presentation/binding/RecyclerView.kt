package com.kakao.presentation.binding

import android.os.Parcelable
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakao.core.error.ViewError
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.model.QuizUIModel
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_list.QuizHistoryAdapter
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_list.QuizHistoryImageAdapter
import java.time.format.DateTimeFormatter

@BindingAdapter("quiz_quizList", "quiz_quizImageMap", "quiz_onClickQuiz")
fun bindAdapter(recyclerView: RecyclerView, quizList: List<QuizUIModel>, quizImageMap: Map<QuizUIModel, List<ImageUIModel>>, onClickQuiz: (QuizUIModel, Parcelable) -> Unit) {
  if (quizList.isEmpty()) recyclerView.visibility = View.GONE

  val itemList = mutableListOf<QuizHistoryAdapter.QuizHistoryItem>()
  quizList.forEachIndexed { index, quiz ->
    quizImageMap[quiz]?.let { imageList ->
      if (index == 0 || quiz.createdAt.toLocalDate() != quizList[index - 1].createdAt.toLocalDate()) {
        val date = quiz.createdAt.format(DateTimeFormatter.ofPattern("yyyy. M. d. (E)"))
        itemList.add(QuizHistoryAdapter.QuizHistoryItem.DateFlagItem(date))
      }
      val scrollState = recyclerView.layoutManager?.onSaveInstanceState() ?: throw ViewError.NoLayoutManager
      itemList.add(QuizHistoryAdapter.QuizHistoryItem.QuizItem(quiz, imageList) { onClickQuiz(quiz, scrollState) })
    }
  }

  (recyclerView.adapter as QuizHistoryAdapter).submitList(itemList)
}

@BindingAdapter("quiz_quiz", "quiz_imageList")
fun bindAdapter(recyclerView: RecyclerView, quiz: QuizUIModel, imageList: List<ImageUIModel>) {
  val itemList = quiz.questions.mapIndexed { index, question ->
    QuizHistoryImageAdapter.QuizHistoryImageItem(imageList[index], question.isCorrect)
  }

  (recyclerView.adapter as QuizHistoryImageAdapter).submitList(itemList)
}