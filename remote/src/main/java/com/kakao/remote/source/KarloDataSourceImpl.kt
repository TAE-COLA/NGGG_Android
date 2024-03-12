package com.kakao.remote.source

import com.kakao.core.error.KarloError
import com.kakao.data.model.GeneratedImageResponseDataModel
import com.kakao.data.model.PromptDataModel
import com.kakao.data.source.KarloDataSource
import com.kakao.remote.model.mapper.asData
import com.kakao.remote.model.mapper.asRemote
import com.kakao.remote.service.KarloService
import javax.inject.Inject

class KarloDataSourceImpl @Inject constructor(
  private val karloService: KarloService
): KarloDataSource {

  override suspend fun getGeneratedImage(
    prompt: PromptDataModel
  ): Result<GeneratedImageResponseDataModel> =
    runCatching {
      karloService
        .getGeneratedImage(prompt.asRemote())
        .body()
        ?.asData() ?: throw KarloError.ApiRequestError
    }

  override suspend fun downloadFile(
    url: String
  ): ByteArray =
    karloService
      .downloadFile(url)
      .body()
      ?.bytes() ?: throw KarloError.DownloadError
}