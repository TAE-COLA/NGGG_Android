package com.kakao.presentation.ui.screen.drawing_robby

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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.presentation.R
import com.kakao.presentation.ui.screen.drawing_robby.DrawingRobbyContract.DrawingRobbyEvent
import com.kakao.presentation.ui.screen.drawing_robby.DrawingRobbyContract.DrawingRobbySideEffect
import com.kakao.presentation.ui.screen.drawing_robby.DrawingRobbyContract.DrawingRobbyState
import com.kakao.presentation.ui.view.Gap
import com.kakao.presentation.ui.view.LargeButton
import com.kakao.presentation.ui.view.LoadingDialog
import com.kakao.presentation.ui.view.PenCountView
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
fun DrawingRobbyRoute(
  popBackStack: () -> Unit,
  navigateToDrawingAI: () -> Unit,
  navigateToDrawingUser: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: DrawingRobbyViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val drawingState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is DrawingRobbySideEffect.PopBackStack -> popBackStack()
        is DrawingRobbySideEffect.NavigateToDrawingAI -> navigateToDrawingAI()
        is DrawingRobbySideEffect.NavigateToDrawingUser -> navigateToDrawingUser()
        is DrawingRobbySideEffect.ShowSnackbar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  if (isLoading) LoadingDialog()
  else DrawingRobbyScreen(
    state = drawingState,
    modifier = modifier,
    onClickBackButton = { viewModel.sendEvent(DrawingRobbyEvent.OnClickBackButton) },
    onClickDrawingAIButton = { viewModel.sendEvent(DrawingRobbyEvent.OnClickDrawingAIButton) },
    onClickDrawingUserButton = { viewModel.sendEvent(DrawingRobbyEvent.OnClickDrawingUserButton) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawingRobbyScreen(
  state: DrawingRobbyState,
  modifier: Modifier = Modifier,
  onClickBackButton: () -> Unit = {},
  onClickDrawingAIButton: () -> Unit = {},
  onClickDrawingUserButton: () -> Unit = {},
  windowSize: WindowSize = LocalWindowSize.current
) {
  Screen(
    modifier = modifier,
    topAppBar = {
      TopAppBar(
        title = { },
        navigationIcon = {
          IconButton(onClick = onClickBackButton) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) {
    Gap(height = if (windowSize <= WindowHeightSizeClass.Compact) 0.dp else 32.dp)
    Column(
      modifier = Modifier.padding(horizontal = 32.dp),
      verticalArrangement = Arrangement.spacedBy(32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      DrawingRobbyTitle()
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) { PenCountView(state.pen) }
      DrawingRobbyButtons(
        onClickDrawingAIButton = onClickDrawingAIButton,
        onClickDrawingUserButton = onClickDrawingUserButton
      )
    }
    Gap(height = if (windowSize <= WindowHeightSizeClass.Compact) 32.dp else 64.dp)
  }
}

@Composable
private fun DrawingRobbyTitle(
  modifier: Modifier = Modifier,
  windowSize: WindowSize = LocalWindowSize.current
) {
  Text(
    stringResource(R.string.drawing_robby_title),
    modifier = modifier,
    color = MaterialTheme.colorScheme.primary,
    style = if (windowSize <= WindowWidthSizeClass.Compact) MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
      else MaterialTheme.typography.displaySmall
  )
}

@Composable
private fun DrawingRobbyButtons(
  onClickDrawingAIButton: () -> Unit,
  onClickDrawingUserButton: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(80.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    DrawingRobbyButton(
      stringResource(R.string.drawing_ai_button),
      onClickButton = onClickDrawingAIButton,
      description = stringResource(R.string.drawing_ai_button_sub)
    )
    DrawingRobbyButton(
      stringResource(R.string.drawing_user_button),
      onClickButton = onClickDrawingUserButton,
      description = stringResource(R.string.drawing_user_button_sub)
    )
  }
}

@Composable
private fun DrawingRobbyButton(
  text: String,
  onClickButton: () -> Unit,
  description: String,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    LargeButton(
      text,
      onClick = onClickButton
    )
    Text(
      description,
      style = MaterialTheme.typography.titleMedium
    )
  }
}

@PhonePortraitPreview
@Composable
private fun DrawingRobbyScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    DrawingRobbyScreen(
      DrawingRobbyState()
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun DrawingRobbyScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    DrawingRobbyScreen(
      DrawingRobbyState()
    )
  }
}

@TabletPortraitPreview
@Composable
private fun DrawingRobbyScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    DrawingRobbyScreen(
      DrawingRobbyState()
    )
  }
}

@TabletLandscapePreview
@Composable
private fun DrawingRobbyScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    DrawingRobbyScreen(
      DrawingRobbyState()
    )
  }
}