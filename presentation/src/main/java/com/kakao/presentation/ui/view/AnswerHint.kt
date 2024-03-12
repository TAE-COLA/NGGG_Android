package com.kakao.presentation.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.presentation.ui.theme.NGGGTheme

@Composable
fun AnswerHint(
  answers: List<String>,
  modifier: Modifier = Modifier,
  hidden: Boolean = true
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterHorizontally)
  ) {
    answers.forEach { answer ->
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        answer.forEach { char ->
          Box(
            modifier = Modifier
              .size(24.dp)
              .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
          ) {
            Text(
              if (hidden) "_" else "$char",
              style = MaterialTheme.typography.titleMedium
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun AnswerHintPreview() {
  NGGGTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        AnswerHint(
          answers = listOf("사자", "고양이")
        )
        AnswerHint(
          answers = listOf("사자", "고양이"),
          hidden = false
        )
      }
    }
  }
}