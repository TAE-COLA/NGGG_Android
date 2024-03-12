package com.kakao.presentation.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.presentation.ui.theme.NGGGTheme

@Composable
fun LargeButton(
  text: String,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  ElevatedButton(
    onClick = onClick,
    modifier = modifier
      .fillMaxWidth()
      .height(80.dp),
    shape = RoundedCornerShape(12.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer,
      contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ),
    elevation = ButtonDefaults.buttonElevation(
      defaultElevation = 6.dp
    )
  ) {
    Text(
      text,
      style = MaterialTheme.typography.titleLarge
    )
  }
}

@Preview
@Composable
private fun LargeButtonPreview() {
  NGGGTheme {
    Surface(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(24.dp)
    ) {
      LargeButton("LargeButton") {}
    }
  }
}