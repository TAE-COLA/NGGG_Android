package com.kakao.presentation.ui.screen.drawing_user

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.presentation.R
import com.kakao.presentation.model.KeywordUIModel
import com.kakao.presentation.ui.screen.drawing_user.DrawingUserContract.*
import com.kakao.presentation.ui.view.ChipBox
import com.kakao.presentation.ui.view.ChipGrid
import com.kakao.presentation.ui.view.GButton
import com.kakao.presentation.ui.view.GTextField
import com.kakao.presentation.ui.view.LoadingDialog
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
fun DrawingUserRoute(
  popBackStack: () -> Unit,
  navigateToDrawingUserSketch: (List<KeywordUIModel>) -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: DrawingUserViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val drawingUserState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is DrawingUserSideEffect.PopBackStack -> popBackStack()
        is DrawingUserSideEffect.NavigateToDrawingUserSketch -> navigateToDrawingUserSketch(drawingUserState.selectedKeywords)
        is DrawingUserSideEffect.ShowSnackbar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  if (isLoading) LoadingDialog()
  else DrawingUserScreen(
    state = drawingUserState,
    modifier = modifier,
    onClickBackButton = { viewModel.sendEvent(DrawingUserEvent.OnClickBackButton) },
    onClickNextButton = { viewModel.sendEvent(DrawingUserEvent.OnClickNextButton) },
    onClickSelectedChipRemove = { viewModel.sendEvent(DrawingUserEvent.OnClickSelectedChipRemove(it)) },
    onChangeSearchKeyword = { viewModel.sendEvent(DrawingUserEvent.OnChangeSearchKeyword(it)) },
    onClickWordChip = { viewModel.sendEvent(DrawingUserEvent.OnClickWordChip(it)) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawingUserScreen(
  state: DrawingUserState,
  modifier: Modifier = Modifier,
  onClickBackButton: () -> Unit = {},
  onClickSelectedChipRemove: (Int) -> Unit = {},
  onClickNextButton: () -> Unit = {},
  onChangeSearchKeyword: (String) -> Unit = {},
  onClickWordChip: (KeywordUIModel) -> Unit = {},
  windowSize: WindowSize = LocalWindowSize.current
) {
  Screen(
    modifier = modifier,
    topAppBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.drawing_user_title)) },
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          stringResource(R.string.drawing_user_sub_text),
          style = MaterialTheme.typography.titleMedium
        )
        ChipBox(
          label = stringResource(R.string.drawing_user_chip_box_label),
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
        GButton(
          text = stringResource(R.string.drawing_user_button_text),
          onClick = onClickNextButton,
          enabled = state.selectedKeywords.isNotEmpty()
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
      label = stringResource(R.string.drawing_user_field_label)
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

@PhonePortraitPreview
@Composable
private fun DrawingUserScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    DrawingUserScreen(
      DrawingUserState()
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun DrawingUserScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    DrawingUserScreen(
      DrawingUserState()
    )
  }
}

@TabletPortraitPreview
@Composable
private fun DrawingUserScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    DrawingUserScreen(
      DrawingUserState()
    )
  }
}

@TabletLandscapePreview
@Composable
private fun DrawingUserScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    DrawingUserScreen(
      DrawingUserState()
    )
  }
}