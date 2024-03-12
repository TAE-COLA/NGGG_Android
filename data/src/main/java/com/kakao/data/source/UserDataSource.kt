package com.kakao.data.source

import com.kakao.data.model.UserDataModel

interface UserDataSource {

  suspend fun getUserIdByKakaoId(kakaoId: String): String?
  suspend fun getUserById(id: String): UserDataModel
  suspend fun insertUser(kakaoId: String, name: String): String
  suspend fun updatePen(id: String, amount: Int)
  suspend fun deleteUser(id: String)
}