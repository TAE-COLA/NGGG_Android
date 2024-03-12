package com.kakao.domain.repository

import com.kakao.domain.model.Image

interface ImageRepository {

  suspend fun getRandomAIImages(limit: Int): List<Image>
  suspend fun getRandomUserImages(limit: Int): List<Image>
  suspend fun getImageById(id: String): Image
  suspend fun insertImage(image: Image): Image
}