package com.kakao.presentation.ui.screen.home

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class HomeContract {
  @Parcelize
  class HomeState : ViewModelContract.State, Parcelable

  sealed interface HomeEvent : ViewModelContract.Event {
    data object OnClickQuizAIRobbyButton : HomeEvent
    data object OnClickQuizUserRobbyButton : HomeEvent
    data object OnClickDrawingRobbyButton : HomeEvent
    data object OnClickQuizHistoryButton : HomeEvent
    data object OnClickMyPageButton : HomeEvent
  }

  sealed interface HomeReduce : ViewModelContract.Reduce

  sealed interface HomeSideEffect : ViewModelContract.SideEffect {
    data object NavigateToQuizAIRobby : HomeSideEffect
    data object NavigateToQuizUserRobby : HomeSideEffect
    data object NavigateToDrawingRobby : HomeSideEffect
    data object NavigateToQuizHistory : HomeSideEffect
    data object NavigateToMyPage : HomeSideEffect
    data class NavigateToQuizFriend(val imageId: String) : HomeSideEffect
    data class ShowSnackBar(val message: UiText) : HomeSideEffect
  }
}