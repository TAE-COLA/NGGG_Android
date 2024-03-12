package com.kakao.remote.source

import com.google.firebase.storage.FirebaseStorage
import com.kakao.data.source.StorageDataSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageDataSourceImpl @Inject constructor(
  firebaseStorage: FirebaseStorage
) : StorageDataSource {

  private val storageReference = firebaseStorage.reference

  override suspend fun uploadFile(
    path: String,
    data: ByteArray
  ): String =
    storageReference.child(path)
      .putBytes(data)
      .await()
      .storage
      .downloadUrl
      .await()
      .toString()
}