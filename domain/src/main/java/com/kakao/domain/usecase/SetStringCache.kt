package com.kakao.domain.usecase

import com.kakao.core.CacheConfig
import com.kakao.domain.repository.FileRepository
import javax.inject.Inject

class SetStringCache @Inject constructor(
  private val fileRepository: FileRepository
) {

  suspend operator fun invoke(string: String) =
    fileRepository.setCache(string.toByteArray(Charsets.UTF_8), CacheConfig.TEMP_STRING_FILE)
}