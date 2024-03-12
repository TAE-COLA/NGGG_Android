package com.kakao.presentation.ui.screen.quiz_history.quiz_history_list

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetImageById
import com.kakao.domain.usecase.GetQuizHistory
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_list.QuizHistoryListContract.QuizHistoryListEvent
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_list.QuizHistoryListContract.QuizHistoryListReduce
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_list.QuizHistoryListContract.QuizHistoryListSideEffect
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_list.QuizHistoryListContract.QuizHistoryListState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.joinAll
import javax.inject.Inject

@HiltViewModel
class QuizHistoryListViewModel @Inject constructor(
  private val getQuizHistory: GetQuizHistory,
  private val getImageById: GetImageById,
  savedStateHandle: SavedStateHandle
) :
  BaseStateViewModel<QuizHistoryListState, QuizHistoryListEvent, QuizHistoryListReduce, QuizHistoryListSideEffect>(
    savedStateHandle
  ) {
  override fun createInitialState(savedState: Parcelable?): QuizHistoryListState {
    return savedState as? QuizHistoryListState ?: QuizHistoryListState()
  }

  init {
    initialize()
  }

  override fun handleEvents(event: QuizHistoryListEvent) {
    when (event) {
      is QuizHistoryListEvent.OnClickBackButton -> sendSideEffect(QuizHistoryListSideEffect.PopBackStack)
      is QuizHistoryListEvent.OnClickQuiz -> {
        updateState(QuizHistoryListReduce.UpdateScrollState(event.scrollState))
        sendSideEffect(QuizHistoryListSideEffect.NavigateToQuizHistoryDetail(event.quiz))
      }
    }
  }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(
      QuizHistoryListSideEffect.ShowSnackbar(
        UiText.DynamicString(
          error.message ?: UnknownError.UNKNOWN
        )
      )
    )
  }

  override fun reduceState(
    state: QuizHistoryListState,
    reduce: QuizHistoryListReduce
  ): QuizHistoryListState =
    when (reduce) {
      is QuizHistoryListReduce.UpdateQuizList -> state.copy(quizList = reduce.quizList)
      is QuizHistoryListReduce.AppendQuizImageSet -> state.copy(
        quizImageMap = state.quizImageMap.toMutableMap().apply {
          this[reduce.quiz] = reduce.imageList
        }
      )
      is QuizHistoryListReduce.UpdateScrollState -> state.copy(scrollState = reduce.state)
    }

  private fun initialize() {
    launch {
      val quizList = getQuizHistory().map { it.asUI() }
      updateState(QuizHistoryListReduce.UpdateQuizList(quizList))

      quizList.map { quiz ->
        launch(false) {
          val imageList = quiz.questions.map { getImageById(it.imageId).asUI(emptyList()) }
          updateState(QuizHistoryListReduce.AppendQuizImageSet(quiz, imageList))
        }
      }.joinAll()
    }
  }
}