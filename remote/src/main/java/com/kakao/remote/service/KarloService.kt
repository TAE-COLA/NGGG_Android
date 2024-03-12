package com.kakao.remote.service

import com.kakao.remote.model.GeneratedImageResponseRemoteModel
import com.kakao.remote.model.PromptRemoteModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

interface KarloService {

  @POST("v2/inference/karlo/t2i")
  suspend fun getGeneratedImage(@Body prompt: PromptRemoteModel): Response<GeneratedImageResponseRemoteModel>

  @Streaming
  @GET
  suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>
}