package com.kakao.presentation.ui.screen.drawing_user_result

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.presentation.R
import com.kakao.presentation.ui.screen.drawing_user_result.DrawingUserResultContract.DrawingUserResultEvent
import com.kakao.presentation.ui.screen.drawing_user_result.DrawingUserResultContract.DrawingUserResultSideEffect
import com.kakao.presentation.ui.screen.drawing_user_result.DrawingUserResultContract.DrawingUserResultState
import com.kakao.presentation.ui.view.ButtonColor
import com.kakao.presentation.ui.view.GButton
import com.kakao.presentation.ui.view.Gap
import com.kakao.presentation.ui.view.LoadingDialog
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
fun DrawingUserResultRoute(
  navigateToHome: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: DrawingUserResultViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val drawingUserResultState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is DrawingUserResultSideEffect.NavigateToHome -> navigateToHome()
        is DrawingUserResultSideEffect.ShowSnackbar -> showSnackbar(it.message.asString(context))
        is DrawingUserResultSideEffect.ShareImageQuiz -> ShareClient.Companion.share(context, it.image.makeFeed(context))
      }
    }
  }

  if (isLoading) LoadingDialog()
  else DrawingUserResultScreen(
    state = drawingUserResultState,
    modifier = modifier,
    onClickHomeButton = { viewModel.sendEvent(DrawingUserResultEvent.OnClickHomeButton) },
    onClickShareButton = { viewModel.sendEvent(DrawingUserResultEvent.OnClickShareButton) }
  )
}

@Composable
private fun DrawingUserResultScreen(
  state: DrawingUserResultState,
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
      stringResource(R.string.drawing_user_result_title),
      style = if (windowSize <= WindowWidthSizeClass.Compact) MaterialTheme.typography.titleMedium
        else MaterialTheme.typography.titleLarge
    )
    Gap(height = 24.dp)
    Image(
      state.drawing.asImageBitmap(),
      contentDescription = "Result Image",
      modifier = Modifier
        .fillAppropriateWidth()
        .aspectRatio(1f)
        .border(2.dp, MaterialTheme.colorScheme.outline),
    )
    Gap(height = 32.dp)
    Text(
      stringResource(R.string.drawing_user_result_sub_text),
      style = MaterialTheme.typography.bodyMedium
    )
    Gap(height = 16.dp)
    GButton(
      text = stringResource(R.string.drawing_user_result_share),
      colors = ButtonColor.Secondary,
      modifier = Modifier.fillAppropriateWidth(),
      onClick = onClickShareButton
    )
    Gap(minHeight = 24.dp)
    GButton(
      text = stringResource(R.string.drawing_user_result_home),
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
private fun DrawingUserResultScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    DrawingUserResultScreen(
      DrawingUserResultState()
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun DrawingUserResultScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    DrawingUserResultScreen(
      DrawingUserResultState()
    )
  }
}

@TabletPortraitPreview
@Composable
private fun DrawingUserResultScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    DrawingUserResultScreen(
      DrawingUserResultState()
    )
  }
}

@TabletLandscapePreview
@Composable
private fun DrawingUserResultScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    DrawingUserResultScreen(
      DrawingUserResultState()
    )
  }
}