package com.kakao.presentation.ui.screen.home

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.error.UnknownError
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.ui.screen.home.HomeContract.HomeEvent
import com.kakao.presentation.ui.screen.home.HomeContract.HomeReduce
import com.kakao.presentation.ui.screen.home.HomeContract.HomeSideEffect
import com.kakao.presentation.ui.screen.home.HomeContract.HomeState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<HomeState, HomeEvent, HomeReduce, HomeSideEffect>(savedStateHandle) {
  override fun createInitialState(savedState: Parcelable?): HomeState {
    return savedState as? HomeState ?: HomeState()
  }

  init {
    initialize()
  }

  override fun handleEvents(event: HomeEvent) {
    when (event) {
      is HomeEvent.OnClickQuizAIRobbyButton -> sendSideEffect(HomeSideEffect.NavigateToQuizAIRobby)
      is HomeEvent.OnClickQuizUserRobbyButton -> sendSideEffect(HomeSideEffect.NavigateToQuizUserRobby)
      is HomeEvent.OnClickDrawingRobbyButton -> sendSideEffect(HomeSideEffect.NavigateToDrawingRobby)
      is HomeEvent.OnClickQuizHistoryButton -> sendSideEffect(HomeSideEffect.NavigateToQuizHistory)
      is HomeEvent.OnClickMyPageButton -> sendSideEffect(HomeSideEffect.NavigateToMyPage)
    }
  }

  override fun reduceState(state: HomeState, reduce: HomeReduce): HomeState = state

  override fun handleErrors(error: Throwable) {
    sendSideEffect(HomeSideEffect.ShowSnackBar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  private fun initialize() {
    savedStateHandle.get<String?>("imageId")?.let { imageId ->
      sendSideEffect(HomeSideEffect.NavigateToQuizFriend(imageId))
    }
  }
}