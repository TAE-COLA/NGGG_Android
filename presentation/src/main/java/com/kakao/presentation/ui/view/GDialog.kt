package com.kakao.presentation.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun GDialog(
  title: String,
  body: String,
  dismissButtonText: String,
  onDismissRequest: () -> Unit,
  confirmButtonText: String,
  onConfirmRequest: () -> Unit,
  modifier: Modifier = Modifier
) {
  val dialogShape = RoundedCornerShape(28.dp)

  Column(
    modifier = modifier
      .width(312.dp)
      .clip(dialogShape)
      .background(MaterialTheme.colorScheme.surface)
      .border(
        width = 2.dp,
        color = MaterialTheme.colorScheme.outlineVariant,
        shape = dialogShape
      )
      .padding(24.dp)
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge
    )
    Text(
      text = body,
      modifier = Modifier.padding(top = 16.dp)
    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 24.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End)
    ) {
      GButton(text = dismissButtonText, onClick = onDismissRequest)
      GButton(text = confirmButtonText, onClick = onConfirmRequest)
    }
  }
}

@Preview
@Composable
private fun GDialogPreview() {
  NGGGTheme {
    Surface {
      GDialog(
        title = "정말 종료하시겠어요?",
        body = "게임 진행 상황을 잃게 됩니다!",
        dismissButtonText = "취소",
        onDismissRequest = {},
        confirmButtonText = "종료",
        onConfirmRequest = {}
      )
    }
  }
}