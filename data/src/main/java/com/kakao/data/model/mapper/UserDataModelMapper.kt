package com.kakao.data.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.UserDataModel
import com.kakao.domain.model.User

object UserDataModelMapper : ModelMapper<UserDataModel, User> {
  override fun asLeft(right: User): UserDataModel =
    UserDataModel(
      kakaoId = right.kakaoId,
      name = right.name,
      pen = right.pen,
      createdAt = right.createdAt,
      deletedAt = right.deletedAt
    )

  override fun asRight(left: UserDataModel): User =
    User(
      kakaoId = left.kakaoId,
      name = left.name,
      pen = left.pen,
      createdAt = left.createdAt,
      deletedAt = left.deletedAt
    )
}

internal fun User.asData(): UserDataModel =
  UserDataModelMapper.asLeft(this)

internal fun UserDataModel.asDomain(): User =
  UserDataModelMapper.asRight(this)