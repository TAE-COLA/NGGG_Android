package com.kakao.domain.usecase

import com.kakao.core.CacheConfig
import com.kakao.core.error.KakaoError
import com.kakao.domain.model.Image
import com.kakao.domain.repository.FileRepository
import com.kakao.domain.repository.ImageRepository
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.StorageRepository
import java.util.UUID
import javax.inject.Inject

class UploadCachedImage @Inject constructor(
  private val storageRepository: StorageRepository,
  private val fileRepository: FileRepository,
  private val imageRepository: ImageRepository,
  private val preferenceRepository: PreferenceRepository
){

  suspend operator fun invoke(answer: String, date: String): Image {
    val uniqueId = UUID.randomUUID()
    val path = "images/$date/$uniqueId.png"

    val byteArray = fileRepository.getCache(CacheConfig.TEMP_IMAGE_FILE)
    val uploadedImageUrl = storageRepository.uploadFile(path, byteArray)
    val imageString = fileRepository.getCache(CacheConfig.TEMP_STRING_FILE).toString(Charsets.UTF_8)

    val userId = preferenceRepository.id ?: throw KakaoError.NoUser
    val image = Image(
      id = "",
      userId = userId,
      answer = answer,
      url = uploadedImageUrl,
      imageString = imageString
    )

    return imageRepository.insertImage(image)
  }
}