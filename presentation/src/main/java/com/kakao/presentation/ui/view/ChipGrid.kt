package com.kakao.presentation.ui.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kakao.presentation.model.KeywordUIModel
import com.kakao.presentation.ui.theme.NGGGTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipGrid(
  keywordList: List<KeywordUIModel>,
  onClickWordChip: (KeywordUIModel) -> Unit,
  modifier: Modifier = Modifier,
) {
  FlowRow(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    modifier = modifier
      .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 12.dp)
  ) {
    keywordList.forEach { keyword ->
      FilterChip(
        selected = true,
        onClick = { onClickWordChip(keyword) },
        label = { Text(keyword.korean) }
      )
    }
  }
}

@Preview
@Composable
private fun ChipGridPreview() {
  NGGGTheme {
    Surface {
      ChipGrid(
        keywordList = listOf(
          KeywordUIModel("lion", "사자"),
          KeywordUIModel("cat", "고양이")
        ),
        onClickWordChip = {}
      )
    }
  }
}