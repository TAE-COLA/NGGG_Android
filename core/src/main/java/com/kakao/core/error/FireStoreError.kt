package com.kakao.core.error

interface FireStoreError {

  data object NoUser : Throwable() {
    override val message: String = "사용자 정보가 없습니다."
  }

  data object InvalidData : Throwable() {
    override val message: String = "데이터를 받아오는 데 실패했습니다."
  }

  data object NoImage : Throwable() {
    override val message: String = "서버에 저장된 이미지가 없거나 부족합니다. 잠시 후 다시 시도해주세요."
  }

  data object NotEnoughPen : Throwable() {
    override val message: String = "펜이 부족합니다."
  }

  data object ImageUploadFailed : Throwable() {
    override val message: String = "이미지 업로드에 실패했습니다."
  }

  data object InvalidQuiz : Throwable() {
    override val message: String = "퀴즈 데이터를 받아오는데 실패했습니다."
  }
}