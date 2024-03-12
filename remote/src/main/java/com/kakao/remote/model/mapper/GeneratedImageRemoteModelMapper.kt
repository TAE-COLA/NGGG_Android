package com.kakao.remote.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.GeneratedImageDataModel
import com.kakao.remote.model.GeneratedImageRemoteModel

object GeneratedImageRemoteModelMapper :
  ModelMapper<GeneratedImageRemoteModel, GeneratedImageDataModel> {
  override fun asLeft(right: GeneratedImageDataModel): GeneratedImageRemoteModel =
    GeneratedImageRemoteModel(
      id = "",
      seed = "",
      image = right.url,
      nsfwContentDetected = false,
      nsfwScore = 0.0
    )

  override fun asRight(left: GeneratedImageRemoteModel): GeneratedImageDataModel =
    GeneratedImageDataModel(
      url = left.image
    )
}

internal fun GeneratedImageDataModel.asRemote(): GeneratedImageRemoteModel =
  GeneratedImageRemoteModelMapper.asLeft(this)

internal fun GeneratedImageRemoteModel.asData(): GeneratedImageDataModel =
  GeneratedImageRemoteModelMapper.asRight(this)