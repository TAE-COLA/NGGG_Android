package com.kakao.presentation.utility

import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.kakao.presentation.ui.theme.NGGGTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
enum class ScreenPreview(val windowSizeClass: WindowSizeClass) {
  PHONE_PORTRAIT(WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))),
  PHONE_LANDSCAPE(WindowSizeClass.calculateFromSize(DpSize(640.dp, 360.dp))),
  TABLET_PORTRAIT(WindowSizeClass.calculateFromSize(DpSize(600.dp, 960.dp))),
  TABLET_LANDSCAPE(WindowSizeClass.calculateFromSize(DpSize(960.dp, 600.dp)))
}

@Composable
fun ScreenPreview(
  screenPreview: ScreenPreview,
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalWindowSize provides WindowSize(screenPreview.windowSizeClass)
  ) {
    NGGGTheme {
      Surface {
        content()
      }
    }
  }
}


@Preview(
  group = "Phone",
  widthDp = 360,
  heightDp = 640
)
annotation class PhonePortraitPreview

@Preview(
  group = "Phone",
  widthDp = 640,
  heightDp = 360
)
annotation class PhoneLandscapePreview

@Preview(
  group = "Tablet",
  widthDp = 600,
  heightDp = 960
)
annotation class TabletPortraitPreview

@Preview(
  group = "Tablet",
  widthDp = 960,
  heightDp = 600
)
annotation class TabletLandscapePreview