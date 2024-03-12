package com.kakao.domain.model

data class Prompt(
  val prompt: String,
  val negativePrompt: String,
  val width: Int,
  val height: Int
)
