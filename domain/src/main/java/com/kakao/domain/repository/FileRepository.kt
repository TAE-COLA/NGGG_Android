package com.kakao.domain.repository

interface FileRepository {

  suspend fun setCache(data: ByteArray, name: String)

  suspend fun getCache(name: String): ByteArray
}