package com.kakao.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GeneratedImageRemoteModel")
data class GeneratedImageRemoteModel(
  val id: String,
  val seed: String,
  val image: String,
  @SerialName("nsfw_content_detected") val nsfwContentDetected: Boolean?,
  @SerialName("nsfw_score") val nsfwScore: Double?
)
