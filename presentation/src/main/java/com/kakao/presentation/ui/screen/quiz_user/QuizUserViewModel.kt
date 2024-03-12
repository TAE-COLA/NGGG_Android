package com.kakao.presentation.ui.screen.quiz_user

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.PromptPack.splitWithDelimiters
import com.kakao.core.QuizConfig
import com.kakao.core.error.FireStoreError
import com.kakao.core.error.NetworkError
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetRandomUserImages
import com.kakao.domain.usecase.InsertUserQuiz
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.mapper.asDomain
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.quiz_user.QuizUserContract.QuizUserEvent
import com.kakao.presentation.ui.screen.quiz_user.QuizUserContract.QuizUserReduce
import com.kakao.presentation.ui.screen.quiz_user.QuizUserContract.QuizUserSideEffect
import com.kakao.presentation.ui.screen.quiz_user.QuizUserContract.QuizUserState
import com.kakao.presentation.utility.UiText
import com.kakao.presentation.utility.submitAnswer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizUserViewModel @Inject constructor(
  private val getRandomUserImages: GetRandomUserImages,
  private val insertUserQuiz: InsertUserQuiz,
  savedStateHandle: SavedStateHandle
) : BaseStateViewModel<QuizUserState, QuizUserEvent, QuizUserReduce, QuizUserSideEffect>(
  savedStateHandle
) {
  override fun createInitialState(savedState: Parcelable?): QuizUserState {
    return savedState as? QuizUserState ?: QuizUserState()
  }

  init {
    launch {
      initialize()
    }
  }

  override fun handleEvents(event: QuizUserEvent) {
    when (event) {
      is QuizUserEvent.OnClickCloseButton -> sendSideEffect(QuizUserSideEffect.OpenExitDialog)
      is QuizUserEvent.OnChangeAnswer -> updateState(QuizUserReduce.UpdateAnswer(event.answer))
      is QuizUserEvent.OnClickSubmitButton -> {
        if (currentState.isCorrect == null) {
          submitAnswer(
            answer = currentState.answer,
            answerList = currentState.imageList[currentState.currentRound].answer,
          ) { isCorrect ->
            updateState(QuizUserReduce.UpdateIsCorrect(isCorrect))
            updateState(QuizUserReduce.UpdateScore(isCorrect))
          }
        } else if (currentState.currentRound < QuizConfig.ROUNDS - 1) {
          updateState(QuizUserReduce.UpdateCurrentRound)
          updateState(QuizUserReduce.UpdateAnswerLog(currentState.answerLog + currentState.answer))
          updateState(QuizUserReduce.UpdateAnswer(""))
          updateState(QuizUserReduce.UpdateIsCorrect(null))
        } else launch {
          saveQuizHistory()
          sendSideEffect(QuizUserSideEffect.NavigateToQuizResult)
        }
      }
      is QuizUserEvent.OnTimeout -> launch {
        saveQuizHistory()
        sendSideEffect(QuizUserSideEffect.NavigateToQuizResult)
      }
    }
  }

  override fun reduceState(state: QuizUserState, reduce: QuizUserReduce): QuizUserState =
    when (reduce) {
      is QuizUserReduce.UpdateImageList -> state.copy(imageList = reduce.imageList)
      is QuizUserReduce.UpdateCurrentRound -> state.copy(currentRound = state.currentRound + 1)
      is QuizUserReduce.UpdateScore -> state.copy(
        score = currentState.score.mapIndexed { index, value ->
          if (index == currentState.currentRound) reduce.isCorrect else value
        }
      )

      is QuizUserReduce.UpdateAnswer -> state.copy(answer = reduce.answer)
      is QuizUserReduce.UpdateIsCorrect -> state.copy(isCorrect = reduce.isCorrect)
      is QuizUserReduce.UpdateAnswerLog -> state.copy(answerLog = reduce.answer)
    }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(QuizUserSideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
    when (error) {
      is FireStoreError.NoImage -> sendSideEffect(QuizUserSideEffect.NavigateToHome)
      is NetworkError.NetworkNotConnected -> sendSideEffect(QuizUserSideEffect.NavigateToHome)
    }
  }

  private suspend fun initialize() {
    val imageList =
      getRandomUserImages(QuizConfig.ROUNDS).map { it.asUI(it.answer.splitWithDelimiters()) }

    updateState(QuizUserReduce.UpdateImageList(imageList))
  }

  private suspend fun saveQuizHistory() {
    val answerLog = (currentState.answerLog + currentState.answer).toMutableList()
    while (answerLog.size < QuizConfig.ROUNDS) {
      answerLog += "(미입력)"
    }
    val score = currentState.score.map { it ?: false }

    insertUserQuiz(
      images = currentState.imageList.map { it.asDomain(it.answer.joinToString(", ")) },
      myAnswer = answerLog,
      score = score
    )
  }
}
