package com.kakao.core

interface ModelMapper<LEFT, RIGHT> {

  fun asRight(left: LEFT): RIGHT
  fun asLeft(right: RIGHT): LEFT
}