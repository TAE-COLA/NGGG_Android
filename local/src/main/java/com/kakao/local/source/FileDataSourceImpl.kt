package com.kakao.local.source

import android.content.Context
import com.kakao.data.source.FileDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class FileDataSourceImpl @Inject constructor(
  @ApplicationContext private val context: Context
) : FileDataSource {

  override suspend fun setCache(data: ByteArray, name: String) =
    File(context.cacheDir, name).run {
      writeBytes(data)
    }


  override suspend fun getCache(name: String): ByteArray =
    File(context.cacheDir, name).run {
      FileInputStream(this).readBytes()
    }
}