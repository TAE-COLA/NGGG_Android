package com.kakao.domain.repository

import com.kakao.domain.model.User

interface UserRepository {

  suspend fun getUserIdByKakaoId(kakaoId: String): String?
  suspend fun getUserById(id: String): User
  suspend fun insertUser(kakaoId: String, name: String): String
  suspend fun updatePen(id: String, amount: Int)
  suspend fun deleteUser(id: String)
}