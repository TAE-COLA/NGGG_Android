package com.kakao.presentation.ui.screen.quiz_user

import android.os.Parcelable
import com.kakao.core.QuizConfig
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class QuizUserContract {
  @Parcelize
  data class QuizUserState(
    val imageList: List<ImageUIModel> = emptyList(),
    val currentRound: Int = 0,
    val score: List<Boolean?> = List(QuizConfig.ROUNDS) { null },
    val answer: String = "",
    val isCorrect: Boolean? = null,  // null일 경우 답이 공개되지 않음
    val answerLog: List<String> = emptyList()
  ) : ViewModelContract.State, Parcelable

  sealed interface QuizUserEvent : ViewModelContract.Event {
    data object OnClickCloseButton : QuizUserEvent
    data class OnChangeAnswer(val answer: String) : QuizUserEvent
    data object OnClickSubmitButton : QuizUserEvent
    data object OnTimeout : QuizUserEvent
  }

  sealed interface QuizUserReduce : ViewModelContract.Reduce {
    data class UpdateImageList(val imageList: List<ImageUIModel>) : QuizUserReduce
    data object UpdateCurrentRound : QuizUserReduce
    data class UpdateScore(val isCorrect: Boolean) : QuizUserReduce
    data class UpdateAnswer(val answer: String) : QuizUserReduce
    data class UpdateIsCorrect(val isCorrect: Boolean?) : QuizUserReduce
    data class UpdateAnswerLog(val answer: List<String>) : QuizUserReduce
  }

  sealed interface QuizUserSideEffect : ViewModelContract.SideEffect {
    data object NavigateToHome : QuizUserSideEffect
    data object NavigateToQuizResult : QuizUserSideEffect
    data object OpenExitDialog : QuizUserSideEffect
    data class ShowSnackbar(val message: UiText) : QuizUserSideEffect
  }
}