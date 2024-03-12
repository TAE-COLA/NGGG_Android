package com.kakao.remote.source

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.core.error.FireStoreError
import com.kakao.data.model.UserDataModel
import com.kakao.data.source.UserDataSource
import com.kakao.remote.model.UserRemoteModel
import com.kakao.remote.model.mapper.asData
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
  fireStore: FirebaseFirestore
) : UserDataSource {

  companion object {
    const val USER_REF_PATH = "User"
    const val COLUMN_KAKAO_ID = "kakao_id"
    const val COLUMN_NAME = "name"
    const val COLUMN_PEN = "pen"
    const val COLUMN_CREATED_AT = "created_at"
    const val COLUMN_DELETED_AT = "deleted_at"
  }

  private val reference = fireStore.collection(USER_REF_PATH)

  override suspend fun getUserIdByKakaoId(
    kakaoId: String
  ): String? =
    reference
      .whereEqualTo(COLUMN_KAKAO_ID, kakaoId)
      .whereEqualTo(COLUMN_DELETED_AT, null)
      .get()
      .await()
      .documents
      .apply { if (size > 1) throw FireStoreError.InvalidData }
      .firstOrNull()
      ?.id

  override suspend fun getUserById(
    id: String
  ): UserDataModel =
    reference
      .document(id)
      .get()
      .await()
      .toObject(UserRemoteModel::class.java)
      ?.asData() ?: throw FireStoreError.NoUser

  override suspend fun insertUser(
    kakaoId: String,
    name: String
  ): String =
    reference
      .add(UserRemoteModel(kakaoId, name))
      .await()
      .id

  override suspend fun updatePen(
    id: String,
    amount: Int
  ) {
    reference
      .document(id)
      .get()
      .await()
      .reference
      .update(COLUMN_PEN, FieldValue.increment(amount.toLong()))
      .await()
  }

  override suspend fun deleteUser(id: String) {
    reference
      .document(id)
      .get()
      .await()
      .reference
      .update(COLUMN_DELETED_AT, Timestamp.now())
      .await()
  }
}
