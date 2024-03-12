package com.kakao.domain.usecase

import com.kakao.core.error.KakaoError
import com.kakao.domain.model.GeneratedImage
import com.kakao.domain.model.Image
import com.kakao.domain.repository.ImageRepository
import com.kakao.domain.repository.KarloRepository
import com.kakao.domain.repository.PreferenceRepository
import com.kakao.domain.repository.StorageRepository
import java.util.UUID
import javax.inject.Inject

class UploadKarloImage @Inject constructor(
  private val storageRepository: StorageRepository,
  private val karloRepository: KarloRepository,
  private val imageRepository: ImageRepository,
  private val preferenceRepository: PreferenceRepository
) {

  suspend operator fun invoke(generatedImage: GeneratedImage, date: String): Image {
    val uniqueId = UUID.randomUUID()
    val path = "images/$date/$uniqueId.png"

    val byteArray = karloRepository.downloadImageFile(generatedImage.url)
    val uploadedImageUrl = storageRepository.uploadFile(path, byteArray)

    val userId = preferenceRepository.id ?: throw KakaoError.NoUser
    val image = Image(
      id = "",
      userId = userId,
      answer = generatedImage.answer,
      url = uploadedImageUrl,
      imageString = null
    )

    return imageRepository.insertImage(image)
  }
}