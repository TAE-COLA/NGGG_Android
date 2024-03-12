package com.kakao.presentation.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.domain.model.Quiz
import com.kakao.presentation.model.QuizUIModel
import com.kakao.presentation.utility.toLocalDateTime
import com.kakao.presentation.utility.toLong

object QuizUIModelMapper : ModelMapper<QuizUIModel, Quiz> {
  override fun asLeft(right: Quiz): QuizUIModel =
    QuizUIModel(
      id = right.id,
      score = right.score,
      questions = right.questions.map { it.asUI() },
      createdAt = right.createdAt.toLocalDateTime()
    )

  override fun asRight(left: QuizUIModel): Quiz =
    Quiz(
      id = left.id,
      score = left.score,
      questions = left.questions.map { it.asData() },
      createdAt = left.createdAt.toLong()
    )
}

internal fun Quiz.asUI(): QuizUIModel =
  QuizUIModelMapper.asLeft(this)

internal fun QuizUIModel.asData(): Quiz =
  QuizUIModelMapper.asRight(this)