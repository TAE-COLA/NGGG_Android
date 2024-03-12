package com.kakao.remote.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.QuestionDataModel
import com.kakao.remote.model.QuestionRemoteModel

object QuestionRemoteModelMapper : ModelMapper<QuestionRemoteModel, QuestionDataModel> {
  override fun asLeft(right: QuestionDataModel): QuestionRemoteModel =
    QuestionRemoteModel(
      imageId = right.imageId,
      myAnswer = right.myAnswer,
      isCorrect = right.isCorrect
    )

  override fun asRight(left: QuestionRemoteModel): QuestionDataModel =
    QuestionDataModel(
      imageId = left.imageId,
      myAnswer = left.myAnswer,
      isCorrect = left.isCorrect
    )
}

internal fun QuestionDataModel.asRemote(): QuestionRemoteModel =
  QuestionRemoteModelMapper.asLeft(this)

internal fun QuestionRemoteModel.asData(): QuestionDataModel =
  QuestionRemoteModelMapper.asRight(this)