package com.kakao.presentation.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.domain.model.Question
import com.kakao.presentation.model.QuestionUIModel

object QuestionUIModelMapper : ModelMapper<QuestionUIModel, Question> {
  override fun asLeft(right: Question): QuestionUIModel =
    QuestionUIModel(
      imageId = right.imageId,
      myAnswer = right.myAnswer,
      isCorrect = right.isCorrect
    )

  override fun asRight(left: QuestionUIModel): Question =
    Question(
      imageId = left.imageId,
      myAnswer = left.myAnswer,
      isCorrect = left.isCorrect
    )
}

internal fun Question.asUI(): QuestionUIModel =
  QuestionUIModelMapper.asLeft(this)

internal fun QuestionUIModel.asData(): Question =
  QuestionUIModelMapper.asRight(this)