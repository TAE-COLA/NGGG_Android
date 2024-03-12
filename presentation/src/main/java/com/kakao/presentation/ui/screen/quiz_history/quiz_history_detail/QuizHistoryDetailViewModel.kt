package com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.PromptPack.splitWithDelimiters
import com.kakao.core.error.QuizHistoryError
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetImageById
import com.kakao.domain.usecase.GetQuizById
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.ImageQuestionUIModel
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail.QuizHistoryDetailContract.QuizHistoryDetailEvent
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail.QuizHistoryDetailContract.QuizHistoryDetailReduce
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail.QuizHistoryDetailContract.QuizHistoryDetailSideEffect
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail.QuizHistoryDetailContract.QuizHistoryDetailState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizHistoryDetailViewModel @Inject constructor(
  private val getQuizById: GetQuizById,
  private val getImageById: GetImageById,
  val savedStateHandle: SavedStateHandle
) :
  BaseStateViewModel<QuizHistoryDetailState, QuizHistoryDetailEvent, QuizHistoryDetailReduce, QuizHistoryDetailSideEffect>(
    savedStateHandle
  ) {
  override fun createInitialState(savedState: Parcelable?): QuizHistoryDetailState {
    return savedState as? QuizHistoryDetailState ?: QuizHistoryDetailState()
  }

  init {
    launch {
      initialize()
    }
  }

  override fun handleEvents(event: QuizHistoryDetailEvent) {
    when (event) {
      is QuizHistoryDetailEvent.OnClickBackButton -> sendSideEffect(QuizHistoryDetailSideEffect.PopBackStack)
    }
  }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(QuizHistoryDetailSideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  override fun reduceState(
    state: QuizHistoryDetailState,
    reduce: QuizHistoryDetailReduce
  ): QuizHistoryDetailState =
    when (reduce) {
      is QuizHistoryDetailReduce.UpdateQuizId -> state.copy(quizId = reduce.quizId)
      is QuizHistoryDetailReduce.UpdateQuiz -> state.copy(quiz = reduce.quiz)
      is QuizHistoryDetailReduce.UpdateImageQuestions -> state.copy(imageQuestions = reduce.imageQuestions)
    }

  private suspend fun initialize() {
    val quizId = savedStateHandle.get<String>("quizId") ?: throw QuizHistoryError.InvalidQuizId
    updateState(QuizHistoryDetailReduce.UpdateQuizId(quizId))

    val quiz = getQuizById(quizId).asUI()
    updateState(QuizHistoryDetailReduce.UpdateQuiz(quiz))

    val imageQuestions = quiz.questions.map {
      val image = getImageById(it.imageId)
      val answer = image.answer.splitWithDelimiters()
      ImageQuestionUIModel(
        question = it,
        image = image.asUI(answer)
      )
    }
    updateState(QuizHistoryDetailReduce.UpdateImageQuestions(imageQuestions))
  }
}