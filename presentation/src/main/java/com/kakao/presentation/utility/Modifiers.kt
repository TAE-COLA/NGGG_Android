package com.kakao.presentation.utility

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp

internal object DefaultValues {
  const val APPROPRIATE_WIDTH_DP = 320
}

internal fun Modifier.fillAppropriateWidth(): Modifier =
  this.widthIn(max = DefaultValues.APPROPRIATE_WIDTH_DP.dp)
    .fillMaxWidth()

internal fun Modifier.clickableWithoutEffect(
  onClick: () -> Unit,
  enabled: Boolean = true
): Modifier = composed {
  this.clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick,
    enabled = enabled
  )
}

internal fun Modifier.clickableWithoutEffect(
  onClick: () -> Unit
): Modifier = composed {
  this.clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick
  )
}