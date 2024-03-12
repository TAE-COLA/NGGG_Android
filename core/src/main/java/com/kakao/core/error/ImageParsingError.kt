package com.kakao.core.error

interface ImageParsingError {

  data object FromStringParsingError : Throwable()
}