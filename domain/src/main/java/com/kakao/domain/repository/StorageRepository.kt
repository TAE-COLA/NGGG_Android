package com.kakao.domain.repository

interface StorageRepository {

  suspend fun uploadFile(path: String, data: ByteArray): String
}