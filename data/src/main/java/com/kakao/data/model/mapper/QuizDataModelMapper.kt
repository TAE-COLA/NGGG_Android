package com.kakao.data.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.QuizDataModel
import com.kakao.domain.model.Quiz

object QuizDataModelMapper : ModelMapper<QuizDataModel, Quiz> {
  override fun asLeft(right: Quiz): QuizDataModel =
    QuizDataModel(
      id = right.id,
      score = right.score,
      questions = right.questions.map { it.asData() },
      createdAt = right.createdAt
    )

  override fun asRight(left: QuizDataModel): Quiz =
    Quiz(
      id = left.id,
      score = left.score,
      questions = left.questions.map { it.asDomain() },
      createdAt = left.createdAt
    )
}

internal fun Quiz.asData(): QuizDataModel =
  QuizDataModelMapper.asLeft(this)

internal fun QuizDataModel.asDomain(): Quiz =
  QuizDataModelMapper.asRight(this)