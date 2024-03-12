package com.kakao.presentation.ui.screen.quiz_result

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class QuizResultContract {
  @Parcelize
  data class QuizResultState(
    val score: Int = 0,
    val pen: Int = 0
  ) : ViewModelContract.State, Parcelable

  sealed interface QuizResultEvent : ViewModelContract.Event {
    data object OnClickHomeButton : QuizResultEvent
  }

  sealed interface QuizResultReduce : ViewModelContract.Reduce {
    data class UpdateScore(val score: Int) : QuizResultReduce
    data class UpdatePen(val pen: Int) : QuizResultReduce
  }

  sealed interface QuizResultSideEffect : ViewModelContract.SideEffect {
    data object NavigateToHome : QuizResultSideEffect
    data class ShowSnackbar(val message: UiText) : QuizResultSideEffect
  }
}