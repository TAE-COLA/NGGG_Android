package com.kakao.remote.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.ImageDataModel
import com.kakao.remote.model.ImageRemoteModel

object ImageRemoteModelMapper : ModelMapper<ImageRemoteModel, ImageDataModel> {
  override fun asLeft(right: ImageDataModel): ImageRemoteModel =
    ImageRemoteModel(
      userId = right.userId,
      answer = right.answer,
      url = right.url,
      imageString = right.imageString
    )

  override fun asRight(left: ImageRemoteModel): ImageDataModel =
    ImageDataModel(
      id = "",
      userId = left.userId,
      answer = left.answer,
      url = left.url,
      imageString = left.imageString
    )
}

internal fun ImageDataModel.asRemote(): ImageRemoteModel =
  ImageRemoteModelMapper.asLeft(this)

internal fun ImageRemoteModel.asData(id: String): ImageDataModel =
  ImageRemoteModelMapper.asRight(this).copy(id = id)