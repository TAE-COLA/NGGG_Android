package com.kakao.domain.repository

import com.kakao.domain.model.GeneratedImage
import com.kakao.domain.model.Prompt

interface KarloRepository {

  suspend fun getGeneratedImage(prompt: Prompt): GeneratedImage
  suspend fun downloadImageFile(imageUrl: String): ByteArray
}