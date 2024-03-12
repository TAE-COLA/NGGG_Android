package com.kakao.presentation.utility

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.presentation.ui.theme.NGGGTheme
import kotlinx.coroutines.delay

@Composable
fun LinearTimer(
  seconds: Float,
  onTimeout: () -> Unit,
  modifier : Modifier = Modifier
) {
  var remainSecond by remember { mutableFloatStateOf(seconds) }

  LaunchedEffect(remainSecond) {
    if (remainSecond == 0f) {
      onTimeout()
    }

    while (remainSecond > 0f) {
      delay(1000L)
      remainSecond--
    }
  }

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .border(3.dp, if (remainSecond > 10f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Text(
        "${remainSecond.toInt()}",
        color = if (remainSecond > 10f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.titleMedium
      )
    }
    LinearProgressIndicator(
      progress = { remainSecond / seconds },
      modifier = Modifier.fillMaxWidth(),
      color = if (remainSecond > 10f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
      strokeCap = StrokeCap.Round
    )
  }
}

@Preview
@Composable
private fun LinearTimerPreview() {
  NGGGTheme {
    Surface {
      LinearTimer(
        15f,
        {},
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}