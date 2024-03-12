package com.kakao.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class UserUIModel (
  val kakaoId: String = "",
  val name: String = "",
  val pen: Int = 0,
  val createdAt: LocalDateTime = LocalDateTime.now(),
  val deletedAt: LocalDateTime? = null
): Parcelable