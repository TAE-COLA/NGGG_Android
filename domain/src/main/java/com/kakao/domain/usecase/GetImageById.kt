package com.kakao.domain.usecase

import com.kakao.domain.model.Image
import com.kakao.domain.repository.ImageRepository
import javax.inject.Inject

class GetImageById @Inject constructor(
  private val imageRepository: ImageRepository
) {

  suspend operator fun invoke(
    id: String
  ): Image =
    imageRepository.getImageById(id)
}