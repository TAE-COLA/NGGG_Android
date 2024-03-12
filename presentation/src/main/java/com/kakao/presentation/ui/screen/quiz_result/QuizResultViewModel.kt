package com.kakao.presentation.ui.screen.quiz_result

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.QuizConfig
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetUser
import com.kakao.domain.usecase.GivePen
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.ui.screen.quiz_result.QuizResultContract.QuizResultEvent
import com.kakao.presentation.ui.screen.quiz_result.QuizResultContract.QuizResultReduce
import com.kakao.presentation.ui.screen.quiz_result.QuizResultContract.QuizResultSideEffect
import com.kakao.presentation.ui.screen.quiz_result.QuizResultContract.QuizResultState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
  private val getUser: GetUser,
  private val givePen: GivePen,
  private val savedStateHandle: SavedStateHandle
) :
  BaseStateViewModel<QuizResultState, QuizResultEvent, QuizResultReduce, QuizResultSideEffect>(
    savedStateHandle
  ) {
  override fun createInitialState(savedState: Parcelable?): QuizResultState {
    return savedState as? QuizResultState ?: QuizResultState()
  }

  init {
    launch {
      initialize()
    }
  }

  override fun handleEvents(event: QuizResultEvent) {
    when (event) {
      is QuizResultEvent.OnClickHomeButton -> sendSideEffect(QuizResultSideEffect.NavigateToHome)
    }
  }

  override fun reduceState(state: QuizResultState, reduce: QuizResultReduce): QuizResultState =
    when (reduce) {
      is QuizResultReduce.UpdateScore -> state.copy(score = reduce.score)
      is QuizResultReduce.UpdatePen -> state.copy(pen = reduce.pen)
    }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(QuizResultSideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  private suspend fun initialize() {
    val score = checkNotNull(savedStateHandle.get<Int?>("score"))
    updateState(QuizResultReduce.UpdateScore(score))

    val pen = getUser().pen

    if (score >= QuizConfig.SCORE_STANDARD) {
      givePen(1)
      updateState(QuizResultReduce.UpdatePen(pen + 1))
    } else {
      updateState(QuizResultReduce.UpdatePen(pen))
    }
  }
}