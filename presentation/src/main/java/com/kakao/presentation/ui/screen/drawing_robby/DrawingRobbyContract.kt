package com.kakao.presentation.ui.screen.drawing_robby

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class DrawingRobbyContract {
  @Parcelize
  data class DrawingRobbyState(
    val pen: Int = 0,
  ) : ViewModelContract.State, Parcelable

  sealed interface DrawingRobbyEvent : ViewModelContract.Event {
    data object OnClickBackButton : DrawingRobbyEvent
    data object OnClickDrawingAIButton : DrawingRobbyEvent
    data object OnClickDrawingUserButton : DrawingRobbyEvent
  }

  sealed interface DrawingRobbyReduce : ViewModelContract.Reduce {
    data class UpdatePenCount(val penCount: Int) : DrawingRobbyReduce
  }

  sealed interface DrawingRobbySideEffect : ViewModelContract.SideEffect {
    data object PopBackStack : DrawingRobbySideEffect
    data object NavigateToDrawingAI : DrawingRobbySideEffect
    data object NavigateToDrawingUser : DrawingRobbySideEffect
    data class ShowSnackbar(val message: UiText) : DrawingRobbySideEffect
  }
}