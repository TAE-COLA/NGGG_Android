package com.kakao.presentation.ui.screen.quiz_history.quiz_history_list

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.model.QuizUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class QuizHistoryListContract {
  @Parcelize
  data class QuizHistoryListState(
    val quizList: List<QuizUIModel> = emptyList(),
    val quizImageMap: Map<QuizUIModel, List<ImageUIModel>> = emptyMap(),
    val scrollState: Parcelable? = null
  ) : ViewModelContract.State, Parcelable

  sealed interface QuizHistoryListEvent : ViewModelContract.Event {
    data object OnClickBackButton : QuizHistoryListEvent
    data class OnClickQuiz(val quiz: QuizUIModel, val scrollState: Parcelable) : QuizHistoryListEvent
  }

  sealed interface QuizHistoryListReduce : ViewModelContract.Reduce {
    data class UpdateQuizList(val quizList: List<QuizUIModel>) : QuizHistoryListReduce
    data class AppendQuizImageSet(val quiz: QuizUIModel, val imageList: List<ImageUIModel>) : QuizHistoryListReduce
    data class UpdateScrollState(val state: Parcelable?) : QuizHistoryListReduce
  }

  sealed interface QuizHistoryListSideEffect : ViewModelContract.SideEffect {
    data object PopBackStack : QuizHistoryListSideEffect
    data class NavigateToQuizHistoryDetail(val quiz: QuizUIModel) : QuizHistoryListSideEffect
    data class ShowSnackbar(val message: UiText) : QuizHistoryListSideEffect
  }
}