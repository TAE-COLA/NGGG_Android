package com.kakao.data.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.QuestionDataModel
import com.kakao.domain.model.Question

object QuestionDataModelMapper : ModelMapper<QuestionDataModel, Question> {
  override fun asLeft(right: Question): QuestionDataModel =
    QuestionDataModel(
      imageId = right.imageId,
      myAnswer = right.myAnswer,
      isCorrect = right.isCorrect
    )

  override fun asRight(left: QuestionDataModel): Question =
    Question(
      imageId = left.imageId,
      myAnswer = left.myAnswer,
      isCorrect = left.isCorrect
    )
}

internal fun Question.asData(): QuestionDataModel =
  QuestionDataModelMapper.asLeft(this)

internal fun QuestionDataModel.asDomain(): Question =
  QuestionDataModelMapper.asRight(this)