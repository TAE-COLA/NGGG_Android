package com.kakao.core.error

import java.io.IOException

interface NetworkError {

  data object NetworkNotConnected : IOException() {
    override val message: String = "네트워크가 연결되지 않았습니다. 네트워크 연결 확인 후 다시 시도해주세요."
  }
}