package com.kakao.data.repository

import com.kakao.data.model.mapper.asData
import com.kakao.data.model.mapper.asDomain
import com.kakao.data.source.ImageDataSource
import com.kakao.domain.model.Image
import com.kakao.domain.repository.ImageRepository
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
  private val imageDataSource: ImageDataSource
): ImageRepository {

  override suspend fun getRandomAIImages(limit: Int): List<Image> =
    imageDataSource.getRandomAIImages(limit).map { it.asDomain() }

  override suspend fun getRandomUserImages(limit: Int): List<Image> =
    imageDataSource.getRandomUserImages(limit).map { it.asDomain() }

  override suspend fun getImageById(id: String): Image =
    imageDataSource.getImageById(id).asDomain()

  override suspend fun insertImage(image: Image): Image =
    imageDataSource.insertImage(image.asData()).asDomain()
}