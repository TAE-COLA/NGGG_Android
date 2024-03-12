package com.kakao.data.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.ImageDataModel
import com.kakao.domain.model.Image

object ImageDataModelMapper : ModelMapper<ImageDataModel, Image> {
  override fun asLeft(right: Image): ImageDataModel =
    ImageDataModel(
      id = right.id,
      userId = right.userId,
      answer = right.answer,
      url = right.url,
      imageString = right.imageString
    )

  override fun asRight(left: ImageDataModel): Image =
    Image(
      id = left.id,
      userId = left.userId,
      answer = left.answer,
      url = left.url,
      imageString = left.imageString
    )
}

internal fun Image.asData(): ImageDataModel =
  ImageDataModelMapper.asLeft(this)

internal fun ImageDataModel.asDomain(): Image =
  ImageDataModelMapper.asRight(this)