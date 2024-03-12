package com.kakao.domain.usecase

import com.kakao.core.CacheConfig
import com.kakao.domain.repository.FileRepository
import javax.inject.Inject

class GetStringCache @Inject constructor(
  private val fileRepository: FileRepository
) {

  suspend operator fun invoke(): String =
    fileRepository.getCache(CacheConfig.TEMP_STRING_FILE).toString(Charsets.UTF_8)
}