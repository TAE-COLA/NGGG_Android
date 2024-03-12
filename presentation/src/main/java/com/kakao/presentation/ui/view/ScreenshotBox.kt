package com.kakao.presentation.ui.view

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.applyCanvas
import com.kakao.core.error.ScreenshotBoxError

@Composable
fun ScreenshotBox(
  screenshotController: ScreenshotController,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  var composableBounds by remember {
    mutableStateOf<Rect?>(null)
  }
  val view = LocalView.current

  DisposableEffect(Unit) {
    screenshotController.callback = {
      var bitmap: Bitmap? = null
      composableBounds?.let { bounds ->
        if (bounds.width == 0f || bounds.height == 0f) return@let
        bitmap =
          Bitmap.createBitmap(bounds.width.toInt(), bounds.height.toInt(), Bitmap.Config.ARGB_8888)
            .applyCanvas {
              translate(-bounds.left, -bounds.top)
              view.draw(this)
            }
      }
      bitmap ?: throw ScreenshotBoxError.ScreenshotFail
    }

    onDispose {
      screenshotController.callback = null
    }
  }

  Box(
    modifier = modifier
      .onGloballyPositioned {
        composableBounds = it.boundsInRoot()
      }
  ) {
    content()
  }
}

@Composable
fun rememberScreenshotController(): ScreenshotController {
  return remember { ScreenshotController() }
}

class ScreenshotController() {

  internal var callback: (() -> Bitmap)? = null

  fun capture(): Bitmap? = callback?.invoke()
}
