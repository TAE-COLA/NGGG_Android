package com.kakao.presentation.ui.screen.my_page

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.UserUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class MyPageContract {
  @Parcelize
  data class MyPageState(
    val user: UserUIModel = UserUIModel(),
  ) : ViewModelContract.State, Parcelable

  sealed interface MyPageEvent : ViewModelContract.Event {
    data object OnClickBackButton : MyPageEvent
    data object OnClickLogoutButton : MyPageEvent
    data object OnClickSignOutButton : MyPageEvent
  }

  sealed interface MyPageReduce : ViewModelContract.Reduce {
    data class UpdateUser(val user: UserUIModel) : MyPageReduce
  }

  sealed interface MyPageSideEffect : ViewModelContract.SideEffect {
    data object PopBackStack : MyPageSideEffect
    data object NavigateToLogin : MyPageSideEffect
    data class ShowSnackbar(val message: UiText) : MyPageSideEffect
  }
}