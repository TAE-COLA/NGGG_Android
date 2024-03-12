package com.kakao.presentation.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kakao.presentation.ui.theme.NGGGTheme

@Composable
fun LoadingDialog(
  modifier: Modifier = Modifier,
  text: String = "Loading..."
) {
  Surface(modifier = modifier.fillMaxSize()) {
    Dialog(onDismissRequest = { }) {
      Column(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.background)
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
      ) {
        CircularProgressIndicator()
        if (text.isNotEmpty()) {
          Text(
            text = text,
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun LoadingDialogPreview() {
  NGGGTheme {
    Surface {
      LoadingDialog()
    }
  }
}