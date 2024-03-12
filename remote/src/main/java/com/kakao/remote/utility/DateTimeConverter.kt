package com.kakao.remote.utility

import com.google.firebase.Timestamp

internal fun Timestamp.toLong() = this.seconds

internal fun Long.toTimestamp() = Timestamp(this, 0)