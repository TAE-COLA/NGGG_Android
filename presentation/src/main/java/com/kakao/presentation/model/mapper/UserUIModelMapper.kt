package com.kakao.presentation.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.domain.model.User
import com.kakao.presentation.model.UserUIModel
import com.kakao.presentation.utility.toLocalDateTime
import com.kakao.presentation.utility.toLong

object UserUIModelMapper : ModelMapper<UserUIModel, User> {
  override fun asLeft(right: User): UserUIModel =
    UserUIModel(
      kakaoId = right.kakaoId,
      name = right.name,
      pen = right.pen,
      createdAt = right.createdAt.toLocalDateTime(),
      deletedAt = right.deletedAt?.toLocalDateTime()
    )

  override fun asRight(left: UserUIModel): User =
    User(
      kakaoId = left.kakaoId,
      name = left.name,
      pen = left.pen,
      createdAt = left.createdAt.toLong(),
      deletedAt = left.deletedAt?.toLong()
    )
}

internal fun User.asUI(): UserUIModel =
  UserUIModelMapper.asLeft(this)

internal fun UserUIModel.asDomain(): User =
  UserUIModelMapper.asRight(this)