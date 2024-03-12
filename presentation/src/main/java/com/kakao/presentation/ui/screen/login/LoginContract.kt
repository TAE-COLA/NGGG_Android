package com.kakao.presentation.ui.screen.login

import android.content.Context
import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class LoginContract {
  @Parcelize
  class LoginState : ViewModelContract.State, Parcelable

  sealed interface LoginEvent : ViewModelContract.Event {
    data class OnClickLoginButton(val context: Context) : LoginEvent
  }

  sealed interface LoginReduce : ViewModelContract.Reduce

  sealed interface LoginSideEffect : ViewModelContract.SideEffect {
    data object NavigateToHome : LoginSideEffect
    data class ShowSnackBar(val message: UiText) : LoginSideEffect
  }
}