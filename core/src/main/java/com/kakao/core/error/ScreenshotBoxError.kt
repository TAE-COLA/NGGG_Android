package com.kakao.core.error

interface ScreenshotBoxError {

  data object ScreenshotFail : Throwable() {
    override val message: String = "스크린샷에 실패하였습니다."
  }
}