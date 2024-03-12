package com.kakao.data.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.PromptDataModel
import com.kakao.domain.model.Prompt

object PromptDataModelMapper : ModelMapper<PromptDataModel, Prompt> {
  override fun asLeft(right: Prompt): PromptDataModel =
    PromptDataModel(
      prompt = right.prompt,
      negativePrompt = right.negativePrompt,
      width = right.width,
      height = right.height
    )

  override fun asRight(left: PromptDataModel): Prompt =
    Prompt(
      prompt = left.prompt,
      negativePrompt = left.negativePrompt,
      width = left.width,
      height = left.height
    )
}

internal fun Prompt.asData(): PromptDataModel =
  PromptDataModelMapper.asLeft(this)

internal fun PromptDataModel.asDomain(): Prompt =
  PromptDataModelMapper.asRight(this)