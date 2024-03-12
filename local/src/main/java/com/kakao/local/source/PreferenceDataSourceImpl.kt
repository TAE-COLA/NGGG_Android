package com.kakao.local.source

import android.content.SharedPreferences
import com.kakao.data.source.PreferenceDataSource
import javax.inject.Inject

class PreferenceDataSourceImpl @Inject constructor(
  private val preference: SharedPreferences
) : PreferenceDataSource {
  companion object {
    const val KAKAO_TOKEN = "kakao_token"
    const val ID = "id"
  }

  override var kakaoToken: String?
    get() = preference.getString(KAKAO_TOKEN, null)
    set(value) { preference.edit().putString(KAKAO_TOKEN, value).apply() }

  override var id: String?
    get() = preference.getString(ID, null)
    set(value) { preference.edit().putString(ID, value).apply() }
}