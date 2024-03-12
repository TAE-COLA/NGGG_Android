package com.kakao.data.source

import com.kakao.data.model.ImageDataModel

interface ImageDataSource {

  suspend fun getRandomAIImages(limit: Int): List<ImageDataModel>
  suspend fun getRandomUserImages(limit: Int): List<ImageDataModel>
  suspend fun getImageById(id: String): ImageDataModel
  suspend fun insertImage(image: ImageDataModel): ImageDataModel
}