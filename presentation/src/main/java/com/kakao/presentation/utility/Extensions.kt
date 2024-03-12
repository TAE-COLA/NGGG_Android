package com.kakao.presentation.utility

import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.util.TypedValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.google.gson.Gson
import com.kakao.presentation.model.LineUIModel
import java.io.ByteArrayOutputStream

fun <T> T.toJson() = Uri.encode(Gson().toJson(this))

fun DrawScope.drawLineUIModel(line: LineUIModel) {
  if (line.linePath.size == 1) {
    val startPoint = Offset(line.linePath.first().x, line.linePath.first().y)
    drawCircle(center = startPoint, color = Color(line.color), radius = line.width / 2)
  } else {
    drawPath(
      line.toPath(),
      color = Color(line.color),
      style = Stroke(
        width = line.width,
        cap = StrokeCap.Round
      )
    )
  }
}

fun Bitmap.toByteArray(): ByteArray {
  val stream = ByteArrayOutputStream()
  compress(Bitmap.CompressFormat.PNG, 100, stream)
  return stream.toByteArray()
}

val Int.floatDp
  get() = Resources.getSystem().displayMetrics?.let { dm ->
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), dm)
  } ?: 0f