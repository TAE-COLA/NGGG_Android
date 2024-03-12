package com.kakao.presentation.ui.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kakao.presentation.R
import com.kakao.presentation.utility.fillAppropriateWidth
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun BorderCoilImage(
  url: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillAppropriateWidth()
      .aspectRatio(1f)
  ) {
    CoilImage(
      imageModel = { url },
      modifier = Modifier
        .align(Alignment.Center)
        .border(2.dp, MaterialTheme.colorScheme.outline),
      imageOptions = ImageOptions(
        contentScale = ContentScale.Fit
      ),
      previewPlaceholder = R.drawable.img_home_picture
    )
  }
}
