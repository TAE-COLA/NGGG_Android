package com.kakao.domain.usecase

import com.kakao.domain.model.Image
import com.kakao.domain.repository.ImageRepository
import javax.inject.Inject

class GetRandomAIImages @Inject constructor(
  private val imageRepository: ImageRepository
) {

  suspend operator fun invoke(
    limit: Int
  ): List<Image> =
    imageRepository.getRandomAIImages(limit)
}