package com.kakao.presentation.base

import android.os.Parcelable

sealed interface ViewModelContract {
  // View를 표현하기 위한 상태
  interface State : ViewModelContract, Parcelable {
    // 상태 저장을 위해 Parcelable로 운용
    fun toParcelable(): Parcelable? = null
  }

  // View에서 일어난 이벤트
  interface Event : ViewModelContract

  // 단순한 상태 변화 내용, Immutable한 상태 유지를 위해 사용
  interface Reduce : ViewModelContract

  // View 에서 처리 (상태 변경 없이 처리 해야 할 것 혹은 외부의 상태 변화)
  interface SideEffect : ViewModelContract
}