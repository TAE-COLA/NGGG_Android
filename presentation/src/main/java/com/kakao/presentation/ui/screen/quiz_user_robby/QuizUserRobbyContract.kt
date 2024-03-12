package com.kakao.presentation.ui.screen.quiz_user_robby

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class QuizUserRobbyContract {
  @Parcelize
  data class QuizUserRobbyState(
    val imageList: List<ImageUIModel> = emptyList(),
  ) : ViewModelContract.State, Parcelable

  sealed interface QuizUserRobbyEvent : ViewModelContract.Event {
    data object OnClickBackButton : QuizUserRobbyEvent
    data object OnClickStartButton : QuizUserRobbyEvent
  }

  sealed interface QuizUserRobbyReduce : ViewModelContract.Reduce {
    data class UpdateImageList(val imageList: List<ImageUIModel>) : QuizUserRobbyReduce
  }

  sealed interface QuizUserRobbySideEffect : ViewModelContract.SideEffect {
    data object PopBackStack : QuizUserRobbySideEffect
    data object NavigateToQuizUser : QuizUserRobbySideEffect
    data class ShowSnackbar(val message: UiText) : QuizUserRobbySideEffect
  }
}