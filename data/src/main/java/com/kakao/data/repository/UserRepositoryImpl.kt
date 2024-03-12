package com.kakao.data.repository

import com.kakao.data.model.mapper.asDomain
import com.kakao.data.source.UserDataSource
import com.kakao.domain.model.User
import com.kakao.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
  private val userDataSource: UserDataSource
): UserRepository {

  override suspend fun getUserIdByKakaoId(kakaoId: String): String? =
    userDataSource.getUserIdByKakaoId(kakaoId)

  override suspend fun getUserById(id: String): User =
    userDataSource.getUserById(id).asDomain()

  override suspend fun insertUser(kakaoId: String, name: String): String =
    userDataSource.insertUser(kakaoId, name)

  override suspend fun updatePen(id: String, amount: Int) =
    userDataSource.updatePen(id, amount)

  override suspend fun deleteUser(id: String) =
    userDataSource.deleteUser(id)
}

