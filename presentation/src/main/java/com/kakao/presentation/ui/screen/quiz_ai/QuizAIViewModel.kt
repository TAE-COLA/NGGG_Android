package com.kakao.presentation.ui.screen.quiz_ai

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.QuizConfig
import com.kakao.core.error.NetworkError
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetGeneratedImage
import com.kakao.domain.usecase.GetKeywords
import com.kakao.domain.usecase.InsertAIQuiz
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.PromptUIModel
import com.kakao.presentation.model.mapper.asDomain
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.quiz_ai.QuizAIContract.QuizAIEvent
import com.kakao.presentation.ui.screen.quiz_ai.QuizAIContract.QuizAIReduce
import com.kakao.presentation.ui.screen.quiz_ai.QuizAIContract.QuizAISideEffect
import com.kakao.presentation.ui.screen.quiz_ai.QuizAIContract.QuizAIState
import com.kakao.presentation.utility.UiText
import com.kakao.presentation.utility.submitAnswer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizAIViewModel @Inject constructor(
  private val getGeneratedImage: GetGeneratedImage,
  private val getKeywords: GetKeywords,
  private val insertAIQuiz: InsertAIQuiz,
  savedStateHandle: SavedStateHandle
) : BaseStateViewModel<QuizAIState, QuizAIEvent, QuizAIReduce, QuizAISideEffect>(savedStateHandle) {
  override fun createInitialState(savedState: Parcelable?): QuizAIState {
    return savedState as? QuizAIState ?: QuizAIState()
  }

  init {
    initialize()
  }

  override fun handleEvents(event: QuizAIEvent) {
    when (event) {
      is QuizAIEvent.OnClickCloseButton -> sendSideEffect(QuizAISideEffect.OpenExitDialog)
      is QuizAIEvent.OnChangeAnswer -> updateState(QuizAIReduce.UpdateAnswer(event.answer))
      is QuizAIEvent.OnClickSubmitButton -> {
        if (currentState.isCorrect == null) {
          submitAnswer(
            answer = currentState.answer,
            answerList = currentState.imageList[currentState.currentRound].answer.map { it.korean }
          ) { isCorrect ->
            updateState(QuizAIReduce.UpdateIsCorrect(isCorrect))
            updateState(QuizAIReduce.UpdateScore(isCorrect))
          }
        } else if (currentState.currentRound < QuizConfig.ROUNDS - 1) {
          updateState(QuizAIReduce.UpdateCurrentRound)
          updateState(QuizAIReduce.UpdateAnswerLog(currentState.answerLog + currentState.answer))
          updateState(QuizAIReduce.UpdateAnswer(""))
          updateState(QuizAIReduce.UpdateIsCorrect(null))
        } else launch {
          saveQuizHistory()
          sendSideEffect(QuizAISideEffect.NavigateToQuizResult)
        }
      }
      is QuizAIEvent.OnTimeout -> launch {
        saveQuizHistory()
        sendSideEffect(QuizAISideEffect.NavigateToQuizResult)
      }
    }
  }

  override fun reduceState(state: QuizAIState, reduce: QuizAIReduce): QuizAIState =
    when (reduce) {
      is QuizAIReduce.AppendImageToImageList -> state.copy(imageList = state.imageList + reduce.image)
      is QuizAIReduce.UpdateCurrentRound -> state.copy(currentRound = state.currentRound + 1)
      is QuizAIReduce.UpdateScore -> state.copy(
        score = currentState.score.mapIndexed { index, value ->
          if (index == currentState.currentRound) reduce.isCorrect else value
        }
      )

      is QuizAIReduce.UpdateAnswer -> state.copy(answer = reduce.answer)
      is QuizAIReduce.UpdateIsCorrect -> state.copy(isCorrect = reduce.isCorrect)
      is QuizAIReduce.UpdateAnswerLog -> state.copy(answerLog = reduce.answer)
    }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(QuizAISideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
    when (error) {
      is NetworkError.NetworkNotConnected -> sendSideEffect(QuizAISideEffect.NavigateToHome)
    }
  }

  private fun initialize() {
    val keywords = getKeywords().map { it.asUI() }

    repeat(QuizConfig.ROUNDS) {
      launch {
        val answers = keywords.shuffled().take((1..2).random())
        val promptText = answers.joinToString(" and ") { it.english }

        val prompt = PromptUIModel(prompt = promptText)
        val generatedImage = getGeneratedImage(prompt.asDomain()).asUI(answers)

        updateState(QuizAIReduce.AppendImageToImageList(generatedImage))
      }
    }
  }

  private suspend fun saveQuizHistory() {
    val answerLog = (currentState.answerLog + currentState.answer).toMutableList()
    while (answerLog.size < QuizConfig.ROUNDS) {
      answerLog += "(미입력)"
    }
    val score = currentState.score.map { it ?: false }

    insertAIQuiz(
      generatedImages = currentState.imageList.map { image ->
        image.asDomain(image.answer.joinToString(", ") { it.korean })
      },
      myAnswer = answerLog,
      score = score
    )
  }
}