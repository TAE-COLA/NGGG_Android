package com.kakao.remote.model.mapper

import com.kakao.core.ModelMapper
import com.kakao.data.model.UserDataModel
import com.kakao.remote.model.UserRemoteModel
import com.kakao.remote.utility.toLong
import com.kakao.remote.utility.toTimestamp

object UserRemoteModelMapper : ModelMapper<UserRemoteModel, UserDataModel> {
  override fun asLeft(right: UserDataModel): UserRemoteModel =
    UserRemoteModel(
      kakaoId = right.kakaoId,
      name = right.name,
      pen = right.pen,
      createdAt = right.createdAt.toTimestamp(),
      deletedAt = right.deletedAt?.toTimestamp()
    )

  override fun asRight(left: UserRemoteModel): UserDataModel =
    UserDataModel(
      kakaoId = left.kakaoId,
      name = left.name,
      pen = left.pen,
      createdAt = left.createdAt.toLong(),
      deletedAt = left.deletedAt?.toLong()
    )
}

internal fun UserDataModel.asRemote(): UserRemoteModel =
  UserRemoteModelMapper.asLeft(this)

internal fun UserRemoteModel.asData(): UserDataModel =
  UserRemoteModelMapper.asRight(this)