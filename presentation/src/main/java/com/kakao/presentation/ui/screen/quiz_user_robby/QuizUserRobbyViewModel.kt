package com.kakao.presentation.ui.screen.quiz_user_robby

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.PromptPack.splitWithDelimiters
import com.kakao.core.error.FireStoreError
import com.kakao.core.error.ImageParsingError
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetRandomUserImages
import com.kakao.presentation.R
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.quiz_user_robby.QuizUserRobbyContract.QuizUserRobbyEvent
import com.kakao.presentation.ui.screen.quiz_user_robby.QuizUserRobbyContract.QuizUserRobbyReduce
import com.kakao.presentation.ui.screen.quiz_user_robby.QuizUserRobbyContract.QuizUserRobbySideEffect
import com.kakao.presentation.ui.screen.quiz_user_robby.QuizUserRobbyContract.QuizUserRobbyState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizUserRobbyViewModel @Inject constructor(
  private val getRandomUserImages: GetRandomUserImages,
  savedStateHandle: SavedStateHandle
) : BaseStateViewModel<QuizUserRobbyState, QuizUserRobbyEvent, QuizUserRobbyReduce, QuizUserRobbySideEffect>(savedStateHandle) {
  override fun createInitialState(savedState: Parcelable?): QuizUserRobbyState {
    return savedState as? QuizUserRobbyState ?: QuizUserRobbyState()
  }

  init {
    launch {
      initialize()
    }
  }

  override fun handleEvents(event: QuizUserRobbyEvent) {
    when (event) {
      is QuizUserRobbyEvent.OnClickBackButton -> sendSideEffect(QuizUserRobbySideEffect.PopBackStack)
      is QuizUserRobbyEvent.OnClickStartButton -> sendSideEffect(QuizUserRobbySideEffect.NavigateToQuizUser)
    }
  }

  override fun reduceState(state: QuizUserRobbyState, reduce: QuizUserRobbyReduce): QuizUserRobbyState =
    when (reduce) {
      is QuizUserRobbyReduce.UpdateImageList -> state.copy(imageList = reduce.imageList)
    }

  override fun handleErrors(error: Throwable) =
    when(error) {
      is FireStoreError.NoImage -> {
        sendSideEffect(QuizUserRobbySideEffect.PopBackStack)
        sendSideEffect(QuizUserRobbySideEffect.ShowSnackbar(UiText.DynamicString(error.message)))
      }
      is ImageParsingError.FromStringParsingError -> {
        sendSideEffect(QuizUserRobbySideEffect.PopBackStack)
        sendSideEffect(QuizUserRobbySideEffect.ShowSnackbar(UiText.StringResource(R.string.quiz_user_robby_parsing_error)))
      }
      else -> sendSideEffect(QuizUserRobbySideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
    }

  private suspend fun initialize() {
    val imageList = getRandomUserImages(8).map { it.asUI(it.answer.splitWithDelimiters()) }

    updateState(QuizUserRobbyReduce.UpdateImageList(imageList))
  }
}