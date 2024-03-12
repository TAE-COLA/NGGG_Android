package com.kakao.presentation.model

import android.os.Parcelable
import com.kakao.presentation.utility.JsonNavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class KeywordUIModel(
  val english: String = "",
  val korean: String = ""
) : Parcelable

class KeywordUIModelArgType : JsonNavType<List<KeywordUIModel>>() {
  override fun fromJsonParse(value: String): List<KeywordUIModel> =
    Json.decodeFromString(ListSerializer(KeywordUIModel.serializer()), value)

  override fun List<KeywordUIModel>.getJsonParse(): String =
    Json.encodeToString(ListSerializer(KeywordUIModel.serializer()), this)
}