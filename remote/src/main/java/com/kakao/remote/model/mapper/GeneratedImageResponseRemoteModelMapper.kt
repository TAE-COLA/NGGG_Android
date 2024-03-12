package com.kakao.remote.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.GeneratedImageResponseDataModel
import com.kakao.remote.model.GeneratedImageResponseRemoteModel

object GeneratedImageResponseRemoteModelMapper :
  ModelMapper<GeneratedImageResponseRemoteModel, GeneratedImageResponseDataModel> {
  override fun asLeft(right: GeneratedImageResponseDataModel): GeneratedImageResponseRemoteModel =
    GeneratedImageResponseRemoteModel(
      id = right.id,
      modelVersion = right.modelVersion,
      images = right.images.map { it.asRemote() }
    )

  override fun asRight(left: GeneratedImageResponseRemoteModel): GeneratedImageResponseDataModel =
    GeneratedImageResponseDataModel(
      id = left.id,
      modelVersion = left.modelVersion,
      images = left.images.map { it.asData() }
    )
}

internal fun GeneratedImageResponseDataModel.asRemote(): GeneratedImageResponseRemoteModel =
  GeneratedImageResponseRemoteModelMapper.asLeft(this)

internal fun GeneratedImageResponseRemoteModel.asData(): GeneratedImageResponseDataModel =
  GeneratedImageResponseRemoteModelMapper.asRight(this)