package com.kakao.presentation.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.core.PromptPack
import com.kakao.domain.model.Prompt
import com.kakao.presentation.model.PromptUIModel

object PromptUIModelMapper : ModelMapper<PromptUIModel, Prompt> {
  override fun asLeft(right: Prompt): PromptUIModel =
    PromptUIModel(
      prompt = right.prompt
    )

  override fun asRight(left: PromptUIModel): Prompt =
    Prompt(
      prompt = "${left.prompt}, ${PromptPack.DRAWN_BY_CRAYON}",
      negativePrompt = "${PromptPack.NegativePrompt.LOW_QUALITY}, ${PromptPack.NegativePrompt.MISSING_BODY}, ${PromptPack.NegativePrompt.EXTRA_BODY}, ${PromptPack.NegativePrompt.UGLY}",
      width = (PromptPack.MIN_WIDTH_DIVIDED_BY_8..PromptPack.MAX_WIDTH_DIVIDED_BY_8).random() * 8,
      height = (PromptPack.MIN_HEIGHT_DIVIDED_BY_8..PromptPack.MAX_HEIGHT_DIVIDED_BY_8).random() * 8
    )
}

internal fun Prompt.asUI(): PromptUIModel =
  PromptUIModelMapper.asLeft(this)

internal fun PromptUIModel.asDomain(): Prompt =
  PromptUIModelMapper.asRight(this)