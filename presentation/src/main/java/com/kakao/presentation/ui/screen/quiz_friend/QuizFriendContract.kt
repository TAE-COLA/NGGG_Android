package com.kakao.presentation.ui.screen.quiz_friend

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.model.UserUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class QuizFriendContract {
  @Parcelize
  data class QuizFriendState(
    val user: UserUIModel = UserUIModel(),
    val providerName: String = "",
    val image: ImageUIModel = ImageUIModel(),
    val answer: String = "",
    val isCorrect: Boolean? = null  // null일 경우 답이 공개되지 않음
  ) : ViewModelContract.State, Parcelable

  sealed interface QuizFriendEvent : ViewModelContract.Event {
    data object OnClickCloseButton : QuizFriendEvent
    data class OnChangeAnswer(val answer: String) : QuizFriendEvent
    data object OnClickSubmitButton : QuizFriendEvent
  }

  sealed interface QuizFriendReduce : ViewModelContract.Reduce {
    data class UpdateUser(val user: UserUIModel) : QuizFriendReduce
    data class UpdateProviderName(val providerName: String) : QuizFriendReduce
    data class UpdateImage(val image: ImageUIModel) : QuizFriendReduce
    data class UpdateAnswer(val answer: String) : QuizFriendReduce
    data class UpdateIsCorrect(val isCorrect: Boolean?) : QuizFriendReduce
  }

  sealed interface QuizFriendSideEffect : ViewModelContract.SideEffect {
    data object NavigateToHome : QuizFriendSideEffect
    data object OpenExitDialog : QuizFriendSideEffect
    data class ShowSnackbar(val message: UiText) : QuizFriendSideEffect
  }
}