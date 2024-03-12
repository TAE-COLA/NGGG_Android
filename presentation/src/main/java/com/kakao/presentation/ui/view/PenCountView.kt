package com.kakao.presentation.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.presentation.R
import com.kakao.presentation.ui.theme.NGGGTheme

@Composable
fun PenCountView(
  penCount: Int,
  modifier: Modifier = Modifier
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
  ) {
    Box(
      modifier = Modifier
        .size(96.dp, 32.dp)
        .background(
          color = MaterialTheme.colorScheme.secondaryContainer,
          shape = RoundedCornerShape(50)
        )
    ) {
      Text(
        penCount.toString(),
        modifier = Modifier
          .padding(end = 16.dp)
          .align(Alignment.CenterEnd),
        style = MaterialTheme.typography.titleLarge
      )
    }
    Image(
      painterResource(id = R.drawable.img_button_pen),
      contentDescription = null,
      modifier = Modifier
        .padding(start = 8.dp)
        .size(40.dp, 56.dp)
        .align(Alignment.CenterStart)
    )
  }
}

@Preview
@Composable
private fun PenCountPreview() {
  NGGGTheme {
    Surface {
      PenCountView(3)
    }
  }
}