package com.kakao.remote.source

import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.core.ImageConfig
import com.kakao.core.error.FireStoreError
import com.kakao.data.model.ImageDataModel
import com.kakao.data.source.ImageDataSource
import com.kakao.remote.model.ImageRemoteModel
import com.kakao.remote.model.mapper.asData
import com.kakao.remote.model.mapper.asRemote
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageDataSourceImpl @Inject constructor(
  firestore: FirebaseFirestore
): ImageDataSource {

  companion object {
    const val IMAGE_REF_PATH = "Image"
    const val COLUMN_USER_ID = "user_id"
    const val COLUMN_ANSWER = "answer"
    const val COLUMN_URL = "url"
  }

  private val reference = firestore.collection(IMAGE_REF_PATH)

  override suspend fun getRandomAIImages(
    limit: Int
  ): List<ImageDataModel> =
    reference
      .whereEqualTo(COLUMN_USER_ID, ImageConfig.DRAWING_BY_AI_NAME)
      .limit(limit.toLong())
      .get()
      .await()
      .documents
      .apply { if (size < limit) throw FireStoreError.NoImage }
      .shuffled()
      .map { it.toObject(ImageRemoteModel::class.java)?.asData(it.id) ?: throw FireStoreError.NoImage }

  override suspend fun getRandomUserImages(
    limit: Int
  ): List<ImageDataModel> =
    reference
      .whereNotEqualTo(COLUMN_USER_ID, ImageConfig.DRAWING_BY_AI_NAME)
      .get()
      .await()
      .documents
      .apply { if (size < limit) throw FireStoreError.NoImage }
      .shuffled()
      .map { it.toObject(ImageRemoteModel::class.java)?.asData(it.id) ?: throw FireStoreError.NoImage }
      .take(limit)

  override suspend fun getImageById(
    id: String
  ): ImageDataModel =
    reference
      .document(id)
      .get()
      .await()
      .let { it.toObject(ImageRemoteModel::class.java)?.asData(it.id) ?: throw FireStoreError.NoImage }

  override suspend fun insertImage(
    image: ImageDataModel
  ): ImageDataModel =
    reference
      .add(image.asRemote())
      .await()
      .get()
      .await()
      .let { it.toObject(ImageRemoteModel::class.java)?.asData(it.id) ?: throw FireStoreError.ImageUploadFailed }
}
