package com.kakao.presentation.utility

import android.content.Context
class ConfigurationForDisplay(context: Context?) {
  private val configuration = context?.resources?.configuration
  private val screenOrientation = configuration!!.orientation
  private val screenWidth = configuration!!.screenWidthDp
  private val screenHeight = configuration!!.screenHeightDp

  private val configurationList = mutableListOf<() -> Unit>()
  private var isExecuted = false

  fun widthMoreThan(width: Int, configure: () -> Unit): ConfigurationForDisplay {
    configurationList.add {
      if (screenWidth >= width && !isExecuted) {
        configure()
        isExecuted = true
      }
    }
    return this
  }

  fun widthBetween(
    smallerWidth: Int,
    biggerWidth: Int,
    orientation: Int,
    configure: () -> Unit
  ): ConfigurationForDisplay {
    configurationList.add {
      if (screenWidth in smallerWidth..<biggerWidth && screenOrientation == orientation && !isExecuted) {
        configure()
        isExecuted = true
      }
    }
    return this
  }

  fun widthLessThan(
    width: Int,
    orientation: Int,
    configure: () -> Unit
  ): ConfigurationForDisplay {
    configurationList.add {
      if (screenWidth < width && screenOrientation == orientation && !isExecuted) {
        configure()
        isExecuted = true
      }
    }
    return this
  }

  fun heightMoreThan(
    height: Int,
    orientation: Int,
    configure: () -> Unit
  ): ConfigurationForDisplay {
    configurationList.add {
      if (screenHeight >= height && screenOrientation == orientation && !isExecuted) {
        configure()
        isExecuted = true
      }
    }
    return this
  }

  fun heightBetween(
    smallerHeight: Int,
    biggerHeight: Int,
    orientation: Int,
    configure: () -> Unit
  ): ConfigurationForDisplay {
    configurationList.add {
      if (screenHeight in smallerHeight..<biggerHeight && screenOrientation == orientation && !isExecuted) {
        configure()
        isExecuted = true
      }
    }
    return this
  }

  fun heightLessThan(
    height: Int,
    orientation: Int,
    configure: () -> Unit
  ): ConfigurationForDisplay {
    configurationList.add {
      if (screenHeight < height && screenOrientation == orientation && !isExecuted) {
        configure()
        isExecuted = true
      }
    }
    return this
  }

  fun doIfOrientation(orientation: Int, configure: () -> Unit) {
    configurationList.add {
      if (screenOrientation == orientation) configure()
    }
  }

  fun doElse(configure: () -> Unit) {
    if (!isExecuted) configure()
  }

  fun execute() {
    configurationList.forEach { it() }
  }
}