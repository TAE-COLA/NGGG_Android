package com.kakao.presentation.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Path
import com.kakao.core.error.ImageParsingError
import kotlinx.parcelize.Parcelize
import kotlin.math.sqrt

@Parcelize
data class DrawingUIModel(
  val drawing: List<LineUIModel> = emptyList()
) : Parcelable {
  companion object {
    const val SEPARATOR = "/"
    fun fromString(drawingString: String): DrawingUIModel =
      try{
        DrawingUIModel(drawingString.split(SEPARATOR).map { LineUIModel.fromString(it) })
      } catch (error: Exception) {
        throw ImageParsingError.FromStringParsingError
      }
    }

  override fun toString(): String =
    drawing.joinToString(SEPARATOR) { it.toString() }
}

@Parcelize
data class LineUIModel(
  val linePath: List<LinePoint> = emptyList(),
  val color: Long = 0xFF000000,
  val width: Float = 1f
) : Parcelable {

  companion object {
    fun fromString(lineString: String): LineUIModel {
      val lineInfo = lineString.split(" ")
      val color = lineInfo[0].toLong()
      val width = lineInfo[1].toFloat()
      val linePath = lineInfo
        .subList(2, lineInfo.lastIndex)
        .map { LinePoint.fromString(it) }
      return LineUIModel(linePath, color, width)
    }
  }

  fun toPath(): Path = Path().apply {
      if (linePath.size > 1) {
        moveTo(linePath.first().x, linePath.first().y)
        linePath.subList(1, linePath.lastIndex).forEach {
          lineTo(it.x, it.y)
        }
      }
    }

  override fun toString(): String =
    StringBuffer().apply {
      append("$color $width ")

      var prev = LinePoint(-1f, -1f)
      linePath.forEachIndexed { index, point ->
        if (index == 0 || isLongerDistance(prev, linePath[index], 1)) {
          prev = linePath[index]
          append(point.toString())
        }
      }
    }.toString()

  private fun isLongerDistance(prev: LinePoint, current: LinePoint, distance: Int) =
    getDistance(prev, current) > distance

  private fun getDistance(prev: LinePoint, current: LinePoint) =
    sqrt((square(prev.x.toInt() - current.x.toInt()) + square(prev.y.toInt() - current.y.toInt())).toDouble())

  private fun square(num: Int) = num * num
}

@Parcelize
data class LinePoint(
  val x: Float,
  val y: Float
) : Parcelable {

  companion object {
    fun fromString(point: String): LinePoint {
      val info = point.split(",").map { it.toFloat() }
      return LinePoint(info[0], info[1])
    }
  }

  override fun toString(): String = "${x.toInt()},${y.toInt()} "
}