package com.kakao.presentation.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.presentation.R
import com.kakao.presentation.model.KeywordUIModel
import com.kakao.presentation.ui.theme.NGGGTheme

@Composable
fun ChipBox(
  label: String = "",
  chips: List<KeywordUIModel> = emptyList(),
  chipRemoveAvailable: Boolean = true,
  modifier: Modifier = Modifier,
  onClickSelectedChipRemove: (Int) -> Unit = {}
) {
  Box(modifier) {
    Box(
      Modifier
        .padding(top = 8.dp)
        .fillMaxWidth()
        .height(56.dp)
        .border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.outline,
          shape = RoundedCornerShape(4.dp)
        )
    ) {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(chips.size) { index ->
          FilterChip(
            label = {
              Text(
                chips[index].korean,
                style = MaterialTheme.typography.labelLarge
              )
            },
            selected = true,
            modifier = Modifier.height(32.dp),
            onClick = {},
            trailingIcon = {
              if (chipRemoveAvailable) {
                Icon(
                  Icons.Outlined.Close,
                  contentDescription = null,
                  modifier = Modifier
                    .size(InputChipDefaults.AvatarSize)
                    .clickable { onClickSelectedChipRemove(index) }
                )
              }
            }
          )
        }
      }
    }
    Box(
      Modifier
        .padding(start = 12.dp)
        .background(MaterialTheme.colorScheme.surface)
    ) {
      Text(
        label,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(horizontal = 4.dp)
      )
    }
  }
}

@Preview
@Composable
private fun ChipBoxPreview() {
  NGGGTheme {
    Surface {
      ChipBox(
        label = stringResource(R.string.drawing_ai_selected_word),
        chips = listOf(
          KeywordUIModel("호랑이"),
          KeywordUIModel("사자")
        )
      )
    }
  }
}