package com.kakao.core.error

interface KakaoError {

  data object NoUser : Throwable() {
    override val message: String = "유저 정보를 확인할 수 없습니다. 다시 로그인해주세요."
  }

  data object AccessTokenFailed : Throwable() {
    override val message: String = "카카오 액세스 토큰을 발급받지 못했습니다."
  }

  data object NoKakaoToken : Throwable() {
    override val message: String = "카카오 액세스 토큰을 확인할 수 없습니다. 다시 로그인해주세요."
  }
}