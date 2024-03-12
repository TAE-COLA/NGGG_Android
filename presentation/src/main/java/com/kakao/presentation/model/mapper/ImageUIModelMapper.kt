package com.kakao.presentation.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.domain.model.Image
import com.kakao.presentation.model.DrawingUIModel
import com.kakao.presentation.model.ImageUIModel

object ImageUIModelMapper : ModelMapper<ImageUIModel, Image> {
  override fun asLeft(right: Image): ImageUIModel =
    ImageUIModel(
      id = right.id,
      userId = right.userId,
      answer = emptyList(),
      url = right.url,
      drawing = right.imageString?.let { DrawingUIModel.fromString(it) }
    )

  override fun asRight(left: ImageUIModel): Image =
    Image(
      id = left.id,
      userId = left.userId,
      answer = "",
      url = left.url,
      imageString = left.drawing?.toString()
    )
}

internal fun Image.asUI(answer: List<String>): ImageUIModel =
  ImageUIModelMapper.asLeft(this).copy(answer = answer)

internal fun ImageUIModel.asDomain(answer: String): Image =
  ImageUIModelMapper.asRight(this).copy(answer = answer)