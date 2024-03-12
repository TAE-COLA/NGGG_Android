package com.kakao.presentation.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.presentation.ui.theme.NGGGTheme
import com.kakao.presentation.ui.theme.success

@Composable
fun QuizIndicator(
  score: List<Boolean?>,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.height(26.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
    verticalAlignment = Alignment.CenterVertically
  ) {
    score.forEach() { isCorrect ->
      Indicator(
        state = when (isCorrect) {
          null -> IndicatorState.Default
          true -> IndicatorState.Correct
          else -> IndicatorState.Wrong
        }
      )
    }
  }
}

@Composable
private fun Indicator(
  modifier: Modifier = Modifier,
  state: IndicatorState = IndicatorState.Default
) {
  val color = when (state) {
    IndicatorState.Default -> MaterialTheme.colorScheme.outlineVariant
    IndicatorState.Correct -> success
    IndicatorState.Wrong -> MaterialTheme.colorScheme.error
  }

  Box(
    modifier = modifier
      .let {
        if (state == IndicatorState.Default) it
        else it.shadow(8.dp, CircleShape, ambientColor = color, spotColor = color)
      }
      .size(12.dp)
      .background(color, CircleShape)
  )
}

private enum class IndicatorState { Default, Correct, Wrong }

@Preview
@Composable
private fun QuizIndicatorPreview() {
  NGGGTheme {
    Surface {
      QuizIndicator(
        score = listOf(true, false, true, false, null, null, null, null, null, null)
      )
    }
  }
}