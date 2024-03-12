package com.kakao.presentation.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.kakao.presentation.R
import com.kakao.presentation.ui.theme.NGGGTheme

@Composable
fun KakaoLoginButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  size: KakaoLoginButtonSize = KakaoLoginButtonSize.Small,
  density: Density = LocalDensity.current
) {
  val containerHeightDp = density.run { (90 * size.scale).toDp() }
  val containerRadiusDp = density.run { (12 * size.scale).toDp() }
  val paddingDp = density.run { (16 * size.scale).toDp() }
  val symbolHeightDp = density.run { (30 * size.scale).toDp() }
  val labelSizeSp = density.run { (30 * size.scale).toSp() }

  val containerColor = Color(0xFFFEE500)
  val symbolColor = Color(0xFF000000)
  val labelColor = Color(0xFF000000).copy(alpha = 0.8f)

  Row(
    modifier = modifier
      .height(containerHeightDp)
      .clip(RoundedCornerShape(containerRadiusDp))
      .background(containerColor)
      .clickable { onClick() }
      .padding(horizontal = paddingDp),
    horizontalArrangement = Arrangement.spacedBy(paddingDp  , alignment = Alignment.CenterHorizontally),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      painter = painterResource(R.drawable.ic_kakao_symbol),
      contentDescription = "Kakao Symbol",
      modifier = Modifier.size(symbolHeightDp),
      tint = symbolColor
    )
    Text(
      stringResource(R.string.kakao_login),
      color = labelColor,
      fontSize = labelSizeSp
    )
  }
}

enum class KakaoLoginButtonSize(val scale: Float) {
  Small(1f),
  Medium(1.5f),
  Big(2f)
}

@Preview
@Composable
private fun KakaoLoginButtonPreview() {
  NGGGTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        KakaoLoginButton(
          onClick = {},
          modifier = Modifier.width(200.dp),
          size = KakaoLoginButtonSize.Small
        )
        KakaoLoginButton(
          onClick = {},
          modifier = Modifier.width(300.dp),
          size = KakaoLoginButtonSize.Medium
        )
        KakaoLoginButton(
          onClick = {},
          modifier = Modifier.width(400.dp),
          size = KakaoLoginButtonSize.Big
        )
      }
    }
  }
}