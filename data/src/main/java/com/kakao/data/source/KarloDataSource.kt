package com.kakao.data.source

import com.kakao.data.model.GeneratedImageResponseDataModel
import com.kakao.data.model.PromptDataModel

interface KarloDataSource {

  suspend fun getGeneratedImage(prompt: PromptDataModel): Result<GeneratedImageResponseDataModel>
  suspend fun downloadFile(url: String): ByteArray
}