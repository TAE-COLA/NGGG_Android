package com.kakao.core.error

interface ViewError {
  data object InvalidViewHolder : Throwable() {
    override val message: String = "잘못된 viewType입니다."
  }

  data object NoLayoutManager : Throwable() {
    override val message: String = "LayoutManager가 존재하지 않습니다."
  }
}