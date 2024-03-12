package com.kakao.presentation.ui.screen.quiz_ai_robby

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class QuizAIRobbyContract {
  @Parcelize
  data class QuizAIRobbyState(
    val imageList: List<ImageUIModel> = emptyList(),
  ) : ViewModelContract.State, Parcelable

  sealed interface QuizAIRobbyEvent : ViewModelContract.Event {
    data object OnClickBackButton : QuizAIRobbyEvent
    data object OnClickStartButton : QuizAIRobbyEvent
  }

  sealed interface QuizAIRobbyReduce : ViewModelContract.Reduce {
    data class UpdateImageList(val imageList: List<ImageUIModel>) : QuizAIRobbyReduce
  }

  sealed interface QuizAIRobbySideEffect : ViewModelContract.SideEffect {
    data object PopBackStack : QuizAIRobbySideEffect
    data object NavigateToQuizAI : QuizAIRobbySideEffect
    data class ShowSnackbar(val message: UiText) : QuizAIRobbySideEffect
  }
}