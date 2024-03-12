package com.kakao.remote.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.PromptDataModel
import com.kakao.remote.model.PromptRemoteModel

object PromptRemoteModelMapper : ModelMapper<PromptRemoteModel, PromptDataModel> {
  override fun asLeft(right: PromptDataModel): PromptRemoteModel =
    PromptRemoteModel(
      prompt = right.prompt,
      negativePrompt = right.negativePrompt,
      width = right.width,
      height = right.height
    )

  override fun asRight(left: PromptRemoteModel): PromptDataModel =
    PromptDataModel(
      prompt = left.prompt,
      negativePrompt = left.negativePrompt,
      width = left.width,
      height = left.height
    )
}

internal fun PromptDataModel.asRemote(): PromptRemoteModel =
  PromptRemoteModelMapper.asLeft(this)

internal fun PromptRemoteModel.asData(): PromptDataModel =
  PromptRemoteModelMapper.asRight(this)