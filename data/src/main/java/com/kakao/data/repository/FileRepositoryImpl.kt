package com.kakao.data.repository

import com.kakao.data.source.FileDataSource
import com.kakao.domain.repository.FileRepository
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
  private val fileDataSource: FileDataSource
): FileRepository {

  override suspend fun setCache(data: ByteArray, name: String) =
    fileDataSource.setCache(data, name)

  override suspend fun getCache(name: String): ByteArray =
    fileDataSource.getCache(name)
}