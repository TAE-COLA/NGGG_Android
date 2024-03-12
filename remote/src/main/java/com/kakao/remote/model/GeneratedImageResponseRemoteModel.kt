package com.kakao.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GeneratedImageResponse")
data class GeneratedImageResponseRemoteModel(
  val id: String,
  @SerialName("model_version") val modelVersion: String,
  val images: List<GeneratedImageRemoteModel>
)