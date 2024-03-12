package com.kakao.presentation.ui.screen.login

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.error.KakaoError
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetUserIdByKakaoId
import com.kakao.domain.usecase.InsertUser
import com.kakao.domain.usecase.SetUserInfo
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.ui.screen.login.LoginContract.LoginEvent
import com.kakao.presentation.ui.screen.login.LoginContract.LoginReduce
import com.kakao.presentation.ui.screen.login.LoginContract.LoginSideEffect
import com.kakao.presentation.ui.screen.login.LoginContract.LoginState
import com.kakao.presentation.utility.UiText
import com.kakao.presentation.utility.login
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val setUserInfo: SetUserInfo,
  private val getUserIdByKakaoId: GetUserIdByKakaoId,
  private val insertUser: InsertUser,
  savedStateHandle: SavedStateHandle
) : BaseStateViewModel<LoginState, LoginEvent, LoginReduce, LoginSideEffect>(savedStateHandle) {

  override fun createInitialState(savedState: Parcelable?): LoginState {
    return savedState as? LoginState ?: LoginState()
  }

  override fun reduceState(state: LoginState, reduce: LoginReduce): LoginState = state

  override fun handleEvents(event: LoginEvent) {
    when (event) {
      is LoginEvent.OnClickLoginButton -> launch {
        val oAuthToken = UserApiClient.Companion.login(event.context)

        UserApiClient.instance.me { user, error ->
          when {
            error != null -> throw error
            user == null -> throw KakaoError.NoUser
            else -> launch {
              signIn(oAuthToken.accessToken, user.id.toString(), user.kakaoAccount?.profile?.nickname ?: "사용자")
            }
          }
        }
      }
    }
  }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(LoginSideEffect.ShowSnackBar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  private suspend fun signIn(kakaoToken: String, kakaoId: String, name: String) {
    Timber.e("==========$kakaoToken, $kakaoId, $name")
    val userId = getUserIdByKakaoId(kakaoId) ?: insertUser(kakaoId, name)

    setUserInfo(kakaoToken, userId)

    sendSideEffect(LoginSideEffect.NavigateToHome)
  }
}