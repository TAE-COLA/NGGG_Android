package com.kakao.presentation.utility

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.DpSize

class WindowSize(
  private val windowSizeClass: WindowSizeClass
): Comparable<Any> {
  override fun compareTo(other: Any): Int {
    return when (other) {
      is WindowWidthSizeClass -> when {
        windowSizeClass.widthSizeClass > other -> 1
        windowSizeClass.widthSizeClass < other -> -1
        else -> 0
      }

      is WindowHeightSizeClass -> when {
        windowSizeClass.heightSizeClass > other -> 1
        windowSizeClass.heightSizeClass < other -> -1
        else -> 0
      }

      else -> throw IllegalArgumentException("Unsupported type")
    }
  }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal val LocalWindowSize = compositionLocalOf { WindowSize(WindowSizeClass.calculateFromSize(DpSize.Zero)) }