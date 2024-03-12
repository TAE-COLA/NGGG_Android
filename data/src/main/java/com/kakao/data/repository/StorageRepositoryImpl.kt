package com.kakao.data.repository

import com.kakao.data.source.StorageDataSource
import com.kakao.domain.repository.StorageRepository
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
  private val storageDataSource: StorageDataSource
): StorageRepository {

  override suspend fun uploadFile(path: String, data: ByteArray): String =
    storageDataSource.uploadFile(path, data)
}