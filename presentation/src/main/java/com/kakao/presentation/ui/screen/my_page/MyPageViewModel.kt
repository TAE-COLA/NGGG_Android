package com.kakao.presentation.ui.screen.my_page

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.DeleteUser
import com.kakao.domain.usecase.GetUser
import com.kakao.domain.usecase.SetUserInfo
import com.kakao.presentation.R
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.my_page.MyPageContract.MyPageEvent
import com.kakao.presentation.ui.screen.my_page.MyPageContract.MyPageReduce
import com.kakao.presentation.ui.screen.my_page.MyPageContract.MyPageSideEffect
import com.kakao.presentation.ui.screen.my_page.MyPageContract.MyPageState
import com.kakao.presentation.utility.UiText
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
  private val getUser: GetUser,
  private val setUserInfo: SetUserInfo,
  private val deleteUser: DeleteUser,
  savedStateHandle: SavedStateHandle
) : BaseStateViewModel<MyPageState, MyPageEvent, MyPageReduce, MyPageSideEffect>(savedStateHandle) {

  override fun createInitialState(savedState: Parcelable?): MyPageState {
    return savedState as? MyPageState ?: MyPageState()
  }

  init {
    launch {
      initialize()
    }
  }

  override fun handleEvents(event: MyPageEvent) {
    when (event) {
      is MyPageEvent.OnClickBackButton -> sendSideEffect(MyPageSideEffect.PopBackStack)
      is MyPageEvent.OnClickLogoutButton -> logout()
      is MyPageEvent.OnClickSignOutButton -> signOut()
    }
  }

  override fun reduceState(state: MyPageState, reduce: MyPageReduce): MyPageState =
    when (reduce) {
      is MyPageReduce.UpdateUser -> state.copy(user = reduce.user)
    }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(MyPageSideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  private suspend fun initialize() {
    val user = getUser().asUI()

    updateState(MyPageReduce.UpdateUser(user))
  }

  private fun logout() {
    setLoading(true)
    UserApiClient.instance.logout {
      setUserInfo(null, null)

      setLoading(false)
      sendSideEffect(MyPageSideEffect.ShowSnackbar(UiText.StringResource(R.string.my_page_logout_message)))
      sendSideEffect(MyPageSideEffect.NavigateToLogin)
    }
  }

  private fun signOut() {
    setLoading(true)
    launch(false) {
      deleteUser()
      UserApiClient.instance.unlink {
        setUserInfo(null, null)

        setLoading(false)
        sendSideEffect(MyPageSideEffect.ShowSnackbar(UiText.StringResource(R.string.my_page_sign_out_message)))
        sendSideEffect(MyPageSideEffect.NavigateToLogin)
      }
    }
  }
}