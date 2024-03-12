package com.kakao.remote.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.QuizDataModel
import com.kakao.remote.model.QuizRemoteModel
import com.kakao.remote.utility.toLong
import com.kakao.remote.utility.toTimestamp

object QuizRemoteModelMapper : ModelMapper<QuizRemoteModel, QuizDataModel> {
  override fun asLeft(right: QuizDataModel): QuizRemoteModel =
    QuizRemoteModel(
      score = right.score,
      questions = right.questions.map { it.asRemote() },
      createdAt = right.createdAt.toTimestamp()
    )

  override fun asRight(left: QuizRemoteModel): QuizDataModel =
    QuizDataModel(
      id = "",
      score = left.score,
      questions = left.questions.map { it.asData() },
      createdAt = left.createdAt.toLong()
    )
}

internal fun QuizDataModel.asRemote(): QuizRemoteModel =
  QuizRemoteModelMapper.asLeft(this)

internal fun QuizRemoteModel.asData(id: String): QuizDataModel =
  QuizRemoteModelMapper.asRight(this).copy(id = id)