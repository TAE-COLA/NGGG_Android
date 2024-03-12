package com.kakao.presentation.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.presentation.ui.theme.NGGGTheme

@Composable
fun GButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  colors: ButtonColor = ButtonColor.Primary,
  elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
  border: BorderStroke? = null,
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
  val containerColor = when (colors) {
    ButtonColor.Primary -> MaterialTheme.colorScheme.primary
    ButtonColor.Secondary -> MaterialTheme.colorScheme.secondary
    ButtonColor.Tertiary -> MaterialTheme.colorScheme.tertiary
    ButtonColor.PrimaryContainer -> MaterialTheme.colorScheme.primaryContainer
    ButtonColor.SecondaryContainer -> MaterialTheme.colorScheme.secondaryContainer
    ButtonColor.TertiaryContainer -> MaterialTheme.colorScheme.tertiaryContainer
    ButtonColor.Error -> MaterialTheme.colorScheme.error
  }
  val contentColor = when (colors) {
    ButtonColor.Primary -> MaterialTheme.colorScheme.onPrimary
    ButtonColor.Secondary -> MaterialTheme.colorScheme.onSecondary
    ButtonColor.Tertiary -> MaterialTheme.colorScheme.onTertiary
    ButtonColor.PrimaryContainer -> MaterialTheme.colorScheme.onPrimaryContainer
    ButtonColor.SecondaryContainer -> MaterialTheme.colorScheme.onSecondaryContainer
    ButtonColor.TertiaryContainer -> MaterialTheme.colorScheme.onTertiaryContainer
    ButtonColor.Error -> MaterialTheme.colorScheme.onError
  }

  Button(
    onClick = onClick,
    modifier = modifier.height(ButtonDefaults.MinHeight),
    enabled = enabled,
    colors = ButtonDefaults.buttonColors(
      containerColor = containerColor,
      contentColor = contentColor
    ),
    elevation = elevation,
    border = border,
    contentPadding = contentPadding
  ) {
    Text(text)
  }
}

enum class ButtonColor { Primary, Secondary, Tertiary, PrimaryContainer, SecondaryContainer, TertiaryContainer, Error }

@Preview @Composable
private fun GButtonPreview(){
  NGGGTheme {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(8.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      item {
        GButton(
          text = "Primary",
          onClick = {},
          colors = ButtonColor.Primary
        )
      }
      item {
        GButton(
          text = "Secondary",
          onClick = {},
          colors = ButtonColor.Secondary
        )
      }
      item {
        GButton(
          text = "Tertiary",
          onClick = {},
          colors = ButtonColor.Tertiary
        )
      }
      item {
        GButton(
          text = "Error",
          onClick = {},
          colors = ButtonColor.Error
        )
      }
      item {
        GButton(
          text = "PrimaryContainer",
          onClick = {},
          colors = ButtonColor.PrimaryContainer
        )
      }
      item {
        GButton(
          text = "SecondaryContainer",
          onClick = {},
          colors = ButtonColor.SecondaryContainer
        )
      }
      item {
        GButton(
          text = "TertiaryContainer",
          onClick = {},
          colors = ButtonColor.TertiaryContainer
        )
      }
      item {
        GButton(
          text = "Disabled",
          onClick = {},
          enabled = false
        )
      }
    }
  }
}