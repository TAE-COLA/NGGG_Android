package com.kakao.presentation.ui.screen.quiz_ai

import android.os.Parcelable
import com.kakao.core.QuizConfig
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.GeneratedImageUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class QuizAIContract {
  @Parcelize
  data class QuizAIState(
    val imageList: List<GeneratedImageUIModel> = emptyList(),
    val currentRound: Int = 0,
    val score: List<Boolean?> = List(QuizConfig.ROUNDS) { null },
    val answer: String = "",
    val isCorrect: Boolean? = null,    // null일 경우 답이 공개되지 않음
    val answerLog: List<String> = emptyList()
  ) : ViewModelContract.State, Parcelable

  sealed interface QuizAIEvent : ViewModelContract.Event {
    data object OnClickCloseButton : QuizAIEvent
    data class OnChangeAnswer(val answer: String) : QuizAIEvent
    data object OnClickSubmitButton : QuizAIEvent
    data object OnTimeout : QuizAIEvent
  }

  sealed interface QuizAIReduce : ViewModelContract.Reduce {
    data class AppendImageToImageList(val image: GeneratedImageUIModel) : QuizAIReduce
    data object UpdateCurrentRound : QuizAIReduce
    data class UpdateScore(val isCorrect: Boolean) : QuizAIReduce
    data class UpdateAnswer(val answer: String) : QuizAIReduce
    data class UpdateIsCorrect(val isCorrect: Boolean?) : QuizAIReduce
    data class UpdateAnswerLog(val answer: List<String>) : QuizAIReduce
  }

  sealed interface QuizAISideEffect : ViewModelContract.SideEffect {
    data object NavigateToHome : QuizAISideEffect
    data object NavigateToQuizResult : QuizAISideEffect
    data object OpenExitDialog : QuizAISideEffect
    data class ShowSnackbar(val message: UiText) : QuizAISideEffect
  }
}