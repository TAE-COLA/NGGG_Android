package com.kakao.data.source

interface StorageDataSource {

  suspend fun uploadFile(path: String, data: ByteArray): String
}