package com.kakao.domain.usecase

import com.kakao.core.CacheConfig
import com.kakao.domain.repository.FileRepository
import javax.inject.Inject

class GetImageCache @Inject constructor(
  private val fileRepository: FileRepository
) {

  suspend operator fun invoke() =
    fileRepository.getCache(CacheConfig.TEMP_IMAGE_FILE)
}