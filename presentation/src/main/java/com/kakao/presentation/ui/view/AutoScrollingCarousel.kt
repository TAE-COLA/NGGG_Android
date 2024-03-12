package com.kakao.presentation.ui.view

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kakao.presentation.ui.theme.NGGGTheme
import kotlinx.coroutines.launch
import java.util.UUID

private const val SCROLL_DX = 48f   // 스크롤 목적지, 클수록 속도가 빠름

private class AutoScrollItem<T>(
  val id: String = UUID.randomUUID().toString(),
  val data: T
)

@Composable
fun <T> AutoScrollingCarousel(
  itemList: List<T>,
  modifier: Modifier = Modifier,
  horizontalPadding: Dp = 0.dp,
  content: @Composable (T) -> Unit,
) {
  val lazyListState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  var newItemList by remember { mutableStateOf(itemList.map { AutoScrollItem(data = it) }) }
  val firstVisibleItemIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }

  LazyRow(
    state = lazyListState,
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = horizontalPadding),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    itemsIndexed(
      newItemList, key = { _, item -> item.id }
    ) { index, item ->
      content(item.data)

      if (index == newItemList.lastIndex) {
        val currentList = newItemList

        val firstPart = currentList.subList(0, firstVisibleItemIndex)
        val secondPart = currentList.subList(firstVisibleItemIndex, currentList.size)
        LaunchedEffect(Unit) {
          coroutineScope.launch {
            lazyListState.scrollToItem(
              0,
              maxOf(0, lazyListState.firstVisibleItemScrollOffset - SCROLL_DX.toInt())
            )
          }
        }

        newItemList = (secondPart + firstPart)
      }
    }
  }

  LaunchedEffect(Unit) {
    coroutineScope.launch {
      while (true) {
        lazyListState.autoScroll()
      }
    }
  }
}

suspend fun ScrollableState.autoScroll(
  animationSpec: AnimationSpec<Float> = tween(durationMillis = 800, easing = LinearEasing)
) {
  var previousValue = 0f
  scroll(MutatePriority.PreventUserInput) {
    animate(
      initialValue = 0f,
      targetValue = SCROLL_DX,
      animationSpec = animationSpec
    ) { currentValue, _ ->
      previousValue += scrollBy(currentValue - previousValue)
    }
  }
}


@Preview
@Composable
private fun AutoScrollingLazyRowPreview() {
  NGGGTheme {
    Surface {
      AutoScrollingCarousel(
        itemList = List(8) { it }
      ) {
        Text(
          "$it",
          modifier = Modifier.width(100.dp)
        )
      }
    }
  }
}