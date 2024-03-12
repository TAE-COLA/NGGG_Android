package com.kakao.presentation.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.kakao.presentation.utility.ShakeConfig
import com.kakao.presentation.utility.rememberShakeController
import com.kakao.presentation.utility.shake

@Composable
fun Shaker(
  shakeConfig: ShakeConfig,
  trigger: Boolean,
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit
) {
  val shakeController = rememberShakeController()

  LaunchedEffect(trigger) {
    shakeController.shake(shakeConfig)
  }

  Box(
    modifier = Modifier
      .shake(shakeController)
      .then(modifier)
  ) {
    content()
  }
}