package com.kakao.presentation.ui.screen.drawing_robby

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetUser
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.ui.screen.drawing_robby.DrawingRobbyContract.DrawingRobbyEvent
import com.kakao.presentation.ui.screen.drawing_robby.DrawingRobbyContract.DrawingRobbyReduce
import com.kakao.presentation.ui.screen.drawing_robby.DrawingRobbyContract.DrawingRobbySideEffect
import com.kakao.presentation.ui.screen.drawing_robby.DrawingRobbyContract.DrawingRobbyState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingRobbyViewModel @Inject constructor(
  private val getUser: GetUser,
  savedStateHandle: SavedStateHandle
) :
  BaseStateViewModel<DrawingRobbyState, DrawingRobbyEvent, DrawingRobbyReduce, DrawingRobbySideEffect>(savedStateHandle) {
  override fun createInitialState(savedState: Parcelable?): DrawingRobbyState {
    return savedState as? DrawingRobbyState ?: DrawingRobbyState()
  }

  init {
    initialize()
  }

  override fun handleEvents(event: DrawingRobbyEvent) {
    when (event) {
      is DrawingRobbyEvent.OnClickBackButton -> sendSideEffect(DrawingRobbySideEffect.PopBackStack)
      is DrawingRobbyEvent.OnClickDrawingAIButton -> sendSideEffect(DrawingRobbySideEffect.NavigateToDrawingAI)
      is DrawingRobbyEvent.OnClickDrawingUserButton -> sendSideEffect(DrawingRobbySideEffect.NavigateToDrawingUser)
    }
  }

  override fun reduceState(state: DrawingRobbyState, reduce: DrawingRobbyReduce): DrawingRobbyState =
    when (reduce) {
      is DrawingRobbyReduce.UpdatePenCount -> state.copy(pen = reduce.penCount)
    }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(DrawingRobbySideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  private fun initialize() {
    launch {
      val penCount = getUser().pen

      updateState(DrawingRobbyReduce.UpdatePenCount(penCount))
    }
  }
}