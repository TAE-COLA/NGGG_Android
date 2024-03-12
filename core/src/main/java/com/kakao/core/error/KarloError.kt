package com.kakao.core.error

interface KarloError {

  data object ApiRequestError : Throwable() {
    override val message: String = "API 요청에 실패하였습니다."
  }

  data object DownloadError : Throwable() {
    override val message: String = "이미지 다운로드에 실패하였습니다."
  }
}