package com.kakao.core.error

interface QuizHistoryError {

  data object InvalidQuizId : Throwable() {
    override val message: String = "해당 퀴즈를 찾을 수 없습니다."
  }
}