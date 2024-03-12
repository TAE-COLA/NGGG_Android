package com.kakao.presentation.binding

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kakao.core.ImageConfig
import com.kakao.core.QuizConfig
import com.kakao.presentation.R
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.model.QuizUIModel
import com.kakao.presentation.utility.ConfigurationForDisplay
import java.time.format.DateTimeFormatter

@BindingAdapter("quiz_datetimeOf")
fun setDatetime(textView: TextView, quiz: QuizUIModel) {
  textView.text = quiz.createdAt.format(DateTimeFormatter.ofPattern("yyyy. M. d. (E) h시 m분"))
}

@BindingAdapter("quiz_isSuccessOf", "quiz_success")
fun setVisibility(textView: TextView, quiz: QuizUIModel, success: Boolean) {
  val isSuccess = when {
    (quiz.score >= QuizConfig.SCORE_STANDARD) -> true
    (quiz.questions.size ==1 && quiz.score == 1) -> true
    else -> false
  }
  textView.visibility = if (success == isSuccess) View.VISIBLE else View.GONE
}

@BindingAdapter("quiz_imageList")
fun setQuizType(textView: TextView, imageList: List<ImageUIModel>) {
  textView.text = when {
    imageList.size == 1 -> textView.context.getString(R.string.quiz_history_list_item_category_3)
    imageList.first().userId == ImageConfig.DRAWING_BY_AI_NAME -> textView.context.getString(R.string.quiz_history_list_item_category_1)
    else -> textView.context.getString(R.string.quiz_history_list_item_category_2)
  }
}

@BindingAdapter("quiz_scoreOf")
fun setScoreText(textView: TextView, quiz: QuizUIModel) {
  var textValue = textView.context.getString(R.string.quiz_history_list_item_score, quiz.questions.size, quiz.score)
  ConfigurationForDisplay(textView.context)
    .widthLessThan(480, Configuration.ORIENTATION_PORTRAIT) {
      textValue = textValue.replace("‧", "").trim()
    }.execute()
  textView.text = textValue
}

@SuppressLint("SetTextI18n")
@BindingAdapter("question_myAnswer")
fun setMyAnswer(textView: TextView, myAnswer: String) {
  textView.text = textView.context.getString(R.string.quiz_history_detail_my_answer, myAnswer)
}

@BindingAdapter("question_answer")
fun setQuestionAnswer(textView: TextView, answer: List<String>) {
  textView.text = answer.joinToString(" ")
}