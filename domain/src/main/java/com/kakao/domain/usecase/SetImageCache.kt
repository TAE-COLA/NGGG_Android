package com.kakao.domain.usecase

import com.kakao.core.CacheConfig
import com.kakao.domain.repository.FileRepository
import javax.inject.Inject

class SetImageCache @Inject constructor(
  private val fileRepository: FileRepository
) {

  suspend operator fun invoke(image: ByteArray) =
    fileRepository.setCache(image, CacheConfig.TEMP_IMAGE_FILE)
}