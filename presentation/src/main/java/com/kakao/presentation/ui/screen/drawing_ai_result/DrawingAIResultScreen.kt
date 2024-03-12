package com.kakao.presentation.ui.screen.drawing_ai_result

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import com.kakao.presentation.ui.screen.drawing_ai_result.DrawingAIResultContract.DrawingAIResultEvent
import com.kakao.presentation.ui.screen.drawing_ai_result.DrawingAIResultContract.DrawingAIResultSideEffect
import com.kakao.presentation.ui.screen.drawing_ai_result.DrawingAIResultContract.DrawingAIResultState
import com.kakao.presentation.ui.view.BorderCoilImage
import com.kakao.presentation.ui.view.ButtonColor
import com.kakao.presentation.ui.view.GButton
import com.kakao.presentation.ui.view.Gap
import com.kakao.presentation.ui.view.LoadingDialog
import com.kakao.presentation.ui.view.LoadingScreen
import com.kakao.presentation.ui.view.Screen
import com.kakao.presentation.utility.LocalWindowSize
import com.kakao.presentation.utility.PhoneLandscapePreview
import com.kakao.presentation.utility.PhonePortraitPreview
import com.kakao.presentation.utility.ScreenPreview
import com.kakao.presentation.utility.TabletLandscapePreview
import com.kakao.presentation.utility.TabletPortraitPreview
import com.kakao.presentation.utility.WindowSize
import com.kakao.presentation.utility.fillAppropriateWidth
import com.kakao.presentation.utility.makeFeed
import com.kakao.presentation.utility.share
import com.kakao.sdk.share.ShareClient
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DrawingAIResultRoute(
  navigateToHome: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: DrawingAIResultViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val drawingAIResultState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is DrawingAIResultSideEffect.NavigateToHome -> navigateToHome()
        is DrawingAIResultSideEffect.ShowSnackBar -> showSnackbar(it.message.asString(context))
        is DrawingAIResultSideEffect.ShareImageQuiz -> ShareClient.Companion.share(context, it.image.makeFeed(context))
      }
    }
  }

  if (isLoading) {
    if (drawingAIResultState.image.url.isEmpty()) {
      LoadingScreen(stringResource(R.string.drawing_result_ai_loading))
    } else {
      LoadingDialog()
    }
  } else DrawingAIResultScreen(
    state = drawingAIResultState,
    modifier = modifier,
    onClickHomeButton = { viewModel.sendEvent(DrawingAIResultEvent.OnClickHomeButton) },
    onClickShareButton = { viewModel.sendEvent(DrawingAIResultEvent.OnClickShareButton) }
  )
}

@Composable
private fun DrawingAIResultScreen(
  state: DrawingAIResultState,
  modifier: Modifier = Modifier,
  onClickHomeButton: () -> Unit = {},
  onClickShareButton: () -> Unit = {},
  windowSize: WindowSize = LocalWindowSize.current
) {
  Screen(
    modifier = modifier.padding(32.dp)
  ) {
    Gap()
    Text(
      stringResource(R.string.drawing_result_ai_title),
      style = if (windowSize <= WindowWidthSizeClass.Compact) MaterialTheme.typography.titleMedium
        else MaterialTheme.typography.titleLarge
    )
    Gap(height = 24.dp)
    BorderCoilImage(url = state.image.url)
    Gap(height = 32.dp)
    Text(
      stringResource(R.string.drawing_result_ai_sub_text),
      style = MaterialTheme.typography.bodyMedium
    )
    Gap(height = 16.dp)
    GButton(
      text = stringResource(R.string.drawing_result_ai_share),
      colors = ButtonColor.Secondary,
      modifier = Modifier.fillAppropriateWidth(),
      onClick = onClickShareButton
    )
    Gap(minHeight = 24.dp)
    GButton(
      text = stringResource(R.string.drawing_result_ai_home),
      colors = ButtonColor.Tertiary,
      modifier = Modifier
        .width(128.dp)
        .align(Alignment.CenterHorizontally)
        .padding(bottom = 32.dp),
      onClick = onClickHomeButton
    )
  }
}

@PhonePortraitPreview
@Composable
private fun DrawingAIResultScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    DrawingAIResultScreen(
      DrawingAIResultState()
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun DrawingAIResultScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    DrawingAIResultScreen(
      DrawingAIResultState()
    )
  }
}

@TabletPortraitPreview
@Composable
private fun DrawingAIResultScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    DrawingAIResultScreen(
      DrawingAIResultState()
    )
  }
}

@TabletLandscapePreview
@Composable
private fun DrawingAIResultScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    DrawingAIResultScreen(
      DrawingAIResultState()
    )
  }
}