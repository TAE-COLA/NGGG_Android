package com.kakao.domain.usecase

import com.kakao.domain.model.GeneratedImage
import com.kakao.domain.model.Prompt
import com.kakao.domain.repository.KarloRepository
import javax.inject.Inject

class GetGeneratedImage @Inject constructor(
  private val karloRepository: KarloRepository
) {

  suspend operator fun invoke(
    prompt: Prompt
  ): GeneratedImage =
    karloRepository.getGeneratedImage(prompt)
}