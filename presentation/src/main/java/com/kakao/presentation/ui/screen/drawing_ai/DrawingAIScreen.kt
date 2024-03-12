package com.kakao.presentation.ui.screen.drawing_ai

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.presentation.R
import com.kakao.presentation.model.KeywordUIModel
import com.kakao.presentation.ui.screen.drawing_ai.DrawingAIContract.DrawingAIEvent
import com.kakao.presentation.ui.screen.drawing_ai.DrawingAIContract.DrawingAISideEffect
import com.kakao.presentation.ui.screen.drawing_ai.DrawingAIContract.DrawingAIState
import com.kakao.presentation.ui.view.ChipBox
import com.kakao.presentation.ui.view.ChipGrid
import com.kakao.presentation.ui.view.GButton
import com.kakao.presentation.ui.view.GTextField
import com.kakao.presentation.ui.view.LoadingDialog
import com.kakao.presentation.ui.view.PenCountView
import com.kakao.presentation.ui.view.ResolutionErrorScreen
import com.kakao.presentation.ui.view.Screen
import com.kakao.presentation.utility.LocalWindowSize
import com.kakao.presentation.utility.PhoneLandscapePreview
import com.kakao.presentation.utility.PhonePortraitPreview
import com.kakao.presentation.utility.ScreenPreview
import com.kakao.presentation.utility.TabletLandscapePreview
import com.kakao.presentation.utility.TabletPortraitPreview
import com.kakao.presentation.utility.WindowSize
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DrawingAIRoute(
  popBackStack: () -> Unit,
  navigateToDrawingAIResult: (List<KeywordUIModel>) -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: DrawingAIViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val drawingAIState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is DrawingAISideEffect.PopBackStack -> popBackStack()
        is DrawingAISideEffect.NavigateToDrawingAIResult -> navigateToDrawingAIResult(drawingAIState.selectedKeywords)
        is DrawingAISideEffect.ShowSnackBar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  if (isLoading) LoadingDialog()
  else DrawingAIScreen(
    state = drawingAIState,
    modifier = modifier,
    onChangeSearchKeyword = { viewModel.sendEvent(DrawingAIEvent.OnChangeSearchKeyword(it)) },
    onClickWordChip = { viewModel.sendEvent(DrawingAIEvent.OnClickWordChip(it)) },
    onClickCreateButton = { viewModel.sendEvent(DrawingAIEvent.OnClickCreateButton) },
    onClickSelectedChipRemove = { viewModel.sendEvent(DrawingAIEvent.OnClickSelectedChipRemove(it)) },
    onClickBackButton = { viewModel.sendEvent(DrawingAIEvent.OnClickBackButton) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawingAIScreen(
  state: DrawingAIState,
  modifier: Modifier = Modifier,
  onChangeSearchKeyword: (keyword: String) -> Unit = {},
  onClickCreateButton: () -> Unit = {},
  onClickWordChip: (KeywordUIModel) -> Unit = {},
  onClickSelectedChipRemove: (Int) -> Unit = {},
  onClickBackButton: () -> Unit = {},
  windowSize: WindowSize = LocalWindowSize.current
) {
  Screen(
    modifier = modifier,
    topAppBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.drawing_ai_title)) },
        navigationIcon = {
          IconButton(onClick = onClickBackButton) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) {
    if (windowSize <= WindowHeightSizeClass.Compact) {
      ResolutionErrorScreen(onClickBackButton)
    } else {
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(start = 32.dp, top = 0.dp, end = 32.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text(
          stringResource(R.string.drawing_ai_sub_text),
          style = MaterialTheme.typography.titleMedium
        )
        ChipBox(
          label = stringResource(R.string.drawing_ai_chipbox_label),
          chips = state.selectedKeywords,
          modifier = Modifier.fillMaxWidth(),
          onClickSelectedChipRemove = onClickSelectedChipRemove
        )
        ChipSelector(
          keyword = state.keyword,
          onChangeSearchKeyword = onChangeSearchKeyword,
          filteredList = state.keywordList.filter { word ->
            state.keyword in word.korean &&
              word.korean !in state.selectedKeywords.map { it.korean }
          },
          onClickWordChip = onClickWordChip,
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        )
        ButtonAndPen(
          onClickButton = onClickCreateButton,
          enabled = state.buttonAvailable && state.selectedKeywords.isNotEmpty() && state.pen > 0,
          pen = state.pen,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}

@Composable
private fun ChipSelector(
  keyword: String,
  onChangeSearchKeyword: (String) -> Unit,
  filteredList: List<KeywordUIModel>,
  onClickWordChip: (KeywordUIModel) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    GTextField(
      value = keyword,
      onValueChange = onChangeSearchKeyword,
      modifier = Modifier.fillMaxWidth(),
      label = stringResource(R.string.drawing_ai_text_field_label)
    )
    ChipGrid(
      keywordList = filteredList,
      onClickWordChip = onClickWordChip,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    )
  }
}

@Composable
private fun ButtonAndPen(
  onClickButton: () -> Unit,
  enabled: Boolean,
  pen: Int,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      PenCountView(
        penCount = 0,
        modifier = Modifier.alpha(0f)
      )
      GButton(
        text = stringResource(R.string.drawng_ai_create_button_text),
        onClick = onClickButton,
        enabled = enabled
      )
      PenCountView(
        penCount = pen
      )
    }
    Text(
      stringResource(R.string.drawing_ai_pen_text),
      style = MaterialTheme.typography.labelLarge
    )
  }
}

@PhonePortraitPreview
@Composable
private fun DrawingAIScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    DrawingAIScreen(
      DrawingAIState()
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun DrawingAIScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    DrawingAIScreen(
      DrawingAIState()
    )
  }
}

@TabletPortraitPreview
@Composable
private fun DrawingAIScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    DrawingAIScreen(
      DrawingAIState()
    )
  }
}

@TabletLandscapePreview
@Composable
private fun DrawingAIScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    DrawingAIScreen(
      DrawingAIState()
    )
  }
}