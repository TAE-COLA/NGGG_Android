package com.kakao.data.source

interface FileDataSource {

  suspend fun setCache(data: ByteArray, name: String)

  suspend fun getCache(name: String): ByteArray
}