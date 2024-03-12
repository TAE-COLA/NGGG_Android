package com.kakao.data.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.GeneratedImageDataModel
import com.kakao.domain.model.GeneratedImage

object GeneratedImageDataModelMapper : ModelMapper<GeneratedImageDataModel, GeneratedImage> {
  override fun asLeft(right: GeneratedImage): GeneratedImageDataModel =
    GeneratedImageDataModel(
      url = right.url
    )

  override fun asRight(left: GeneratedImageDataModel): GeneratedImage =
    GeneratedImage(
      answer = "",
      url = left.url
    )
}

internal fun GeneratedImage.asData(): GeneratedImageDataModel =
  GeneratedImageDataModelMapper.asLeft(this)

internal fun GeneratedImageDataModel.asDomain(answer: String): GeneratedImage =
  GeneratedImageDataModelMapper.asRight(this)