package com.kakao.data.repository

import com.kakao.core.PromptPack
import com.kakao.core.error.NetworkError
import com.kakao.data.model.mapper.asData
import com.kakao.data.model.mapper.asDomain
import com.kakao.data.source.KarloDataSource
import com.kakao.domain.model.GeneratedImage
import com.kakao.domain.model.Prompt
import com.kakao.domain.repository.KarloRepository
import java.net.ConnectException
import javax.inject.Inject

class KarloRepositoryImpl @Inject constructor(
  private val karloDataSource: KarloDataSource
): KarloRepository {

  override suspend fun getGeneratedImage(prompt: Prompt): GeneratedImage {
    val result = karloDataSource.getGeneratedImage(prompt.asData())
    val value = result.getOrElse {
      throw if (it is ConnectException) NetworkError.NetworkNotConnected
      else it
    }

    val answer = prompt.prompt.removeSuffix(", ${PromptPack.DRAWN_BY_CRAYON}")
    return value.images.first().asDomain(answer)
  }

  override suspend fun downloadImageFile(imageUrl: String): ByteArray =
    karloDataSource.downloadFile(imageUrl)
}