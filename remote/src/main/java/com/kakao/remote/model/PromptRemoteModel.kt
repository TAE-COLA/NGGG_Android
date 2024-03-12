package com.kakao.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PromptRemoteModel")
data class PromptRemoteModel(
  val prompt: String,
  @SerialName("negative_prompt") val negativePrompt: String,
  val width: Int,
  val height: Int
)
