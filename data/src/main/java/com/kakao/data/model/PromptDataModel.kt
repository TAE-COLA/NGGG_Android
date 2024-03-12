package com.kakao.data.model

data class PromptDataModel(
  val prompt: String,
  val negativePrompt: String,
  val width: Int,
  val height: Int
)
