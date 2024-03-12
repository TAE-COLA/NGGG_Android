package com.kakao.presentation.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.domain.model.Keyword
import com.kakao.presentation.model.KeywordUIModel

object KeywordUIModelMapper : ModelMapper<KeywordUIModel, Keyword> {
  override fun asLeft(right: Keyword): KeywordUIModel =
    KeywordUIModel(
      english = right.english,
      korean = right.korean,
    )

  override fun asRight(left: KeywordUIModel): Keyword =
    Keyword(
      english = left.english,
      korean = left.korean
    )
}

internal fun Keyword.asUI(): KeywordUIModel =
  KeywordUIModelMapper.asLeft(this)

internal fun KeywordUIModel.asDomain(): Keyword =
  KeywordUIModelMapper.asRight(this)