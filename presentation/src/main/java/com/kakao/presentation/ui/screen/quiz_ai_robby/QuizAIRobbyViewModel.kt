package com.kakao.presentation.ui.screen.quiz_ai_robby

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.PromptPack.splitWithDelimiters
import com.kakao.core.error.FireStoreError
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetRandomAIImages
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.quiz_ai_robby.QuizAIRobbyContract.QuizAIRobbyEvent
import com.kakao.presentation.ui.screen.quiz_ai_robby.QuizAIRobbyContract.QuizAIRobbyReduce
import com.kakao.presentation.ui.screen.quiz_ai_robby.QuizAIRobbyContract.QuizAIRobbySideEffect
import com.kakao.presentation.ui.screen.quiz_ai_robby.QuizAIRobbyContract.QuizAIRobbyState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizAIRobbyViewModel @Inject constructor(
  private val getRandomAIImages: GetRandomAIImages,
  savedStateHandle: SavedStateHandle
) : BaseStateViewModel<QuizAIRobbyState, QuizAIRobbyEvent, QuizAIRobbyReduce, QuizAIRobbySideEffect>(savedStateHandle) {
  override fun createInitialState(savedState: Parcelable?): QuizAIRobbyState {
    return savedState as? QuizAIRobbyState ?: QuizAIRobbyState()
  }

  init {
    launch {
      initialize()
    }
  }

  override fun handleEvents(event: QuizAIRobbyEvent) {
    when (event) {
      is QuizAIRobbyEvent.OnClickBackButton -> sendSideEffect(QuizAIRobbySideEffect.PopBackStack)
      is QuizAIRobbyEvent.OnClickStartButton -> sendSideEffect(QuizAIRobbySideEffect.NavigateToQuizAI)
    }
  }

  override fun reduceState(state: QuizAIRobbyState, reduce: QuizAIRobbyReduce): QuizAIRobbyState =
    when (reduce) {
      is QuizAIRobbyReduce.UpdateImageList -> state.copy(imageList = reduce.imageList)
    }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(QuizAIRobbySideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
    when (error) {
      is FireStoreError.NoImage -> sendSideEffect(QuizAIRobbySideEffect.PopBackStack)
    }
  }

  private suspend fun initialize() {
    val imageList = getRandomAIImages(6).map { it.asUI(it.answer.splitWithDelimiters()) }

    updateState(QuizAIRobbyReduce.UpdateImageList(imageList))
  }
}