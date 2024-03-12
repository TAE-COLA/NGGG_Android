package com.kakao.presentation.ui.view

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kakao.presentation.R
import com.kakao.presentation.ui.theme.NGGGTheme

@Composable
fun GTextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  label: String? = null,
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    enabled = enabled,
    label = { if (label != null) Text(label) },
    trailingIcon = {
      if (value.isNotBlank() && enabled) {
        IconButton(onClick = { onValueChange("") }) {
          Icon(
            painterResource(R.drawable.ic_cancel_24),
            contentDescription = "Cancel"
          )
        }
      }
    },
    singleLine = true
  )
}

@Preview
@Composable
private fun GTextFieldPreview() {
  NGGGTheme {
    Surface {
      GTextField(
        value = "안녕!",
        onValueChange = {},
        label = "인사해봐!"
      )
    }
  }
}