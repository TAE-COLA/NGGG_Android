package com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.ImageQuestionUIModel
import com.kakao.presentation.model.QuizUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class QuizHistoryDetailContract {
  @Parcelize
  data class QuizHistoryDetailState(
    val quizId: String = "",
    val quiz: QuizUIModel = QuizUIModel(),
    val imageQuestions: List<ImageQuestionUIModel> = emptyList()
  ) : ViewModelContract.State, Parcelable

  sealed interface QuizHistoryDetailEvent : ViewModelContract.Event {
    data object OnClickBackButton : QuizHistoryDetailEvent
  }

  sealed interface QuizHistoryDetailReduce : ViewModelContract.Reduce {
    data class UpdateQuizId(val quizId: String) : QuizHistoryDetailReduce
    data class UpdateQuiz(val quiz: QuizUIModel) : QuizHistoryDetailReduce
    data class UpdateImageQuestions(val imageQuestions: List<ImageQuestionUIModel>) : QuizHistoryDetailReduce
  }

  sealed interface QuizHistoryDetailSideEffect : ViewModelContract.SideEffect {
    data object PopBackStack : QuizHistoryDetailSideEffect
    data class ShowSnackbar(val message: UiText) : QuizHistoryDetailSideEffect
  }
}