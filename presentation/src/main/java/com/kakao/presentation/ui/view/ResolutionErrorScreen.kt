package com.kakao.presentation.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.presentation.R
import com.kakao.presentation.ui.theme.NGGGTheme

@Composable
fun ResolutionErrorScreen(
  onClickBackButton: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(stringResource(R.string.resolution_error_message))
    GButton(
      stringResource(R.string.resolution_error_button),
      onClick = onClickBackButton
    )
  }
}

@Preview
@Composable
private fun ResolutionErrorScreenPreview() {
  NGGGTheme {
    Surface {
      ResolutionErrorScreen(
        {}
      )
    }
  }
}