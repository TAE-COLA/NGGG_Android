package com.kakao.presentation.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.domain.model.GeneratedImage
import com.kakao.presentation.model.GeneratedImageUIModel
import com.kakao.presentation.model.KeywordUIModel

object GeneratedImageUIModelMapper : ModelMapper<GeneratedImageUIModel, GeneratedImage> {
  override fun asLeft(right: GeneratedImage): GeneratedImageUIModel =
    GeneratedImageUIModel(
      answer = emptyList(),
      url = right.url
    )

  override fun asRight(left: GeneratedImageUIModel): GeneratedImage =
    GeneratedImage(
      answer = "",
      url = left.url
    )
}

internal fun GeneratedImage.asUI(answer: List<KeywordUIModel>): GeneratedImageUIModel =
  GeneratedImageUIModelMapper.asLeft(this).copy(answer = answer)

internal fun GeneratedImageUIModel.asDomain(answer: String): GeneratedImage =
  GeneratedImageUIModelMapper.asRight(this).copy(answer = answer)