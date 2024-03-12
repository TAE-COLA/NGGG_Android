package com.kakao.presentation.ui.screen.quiz_result

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.core.QuizConfig
import com.kakao.core.QuizConfig.SCORE_STANDARD
import com.kakao.presentation.R
import com.kakao.presentation.ui.screen.quiz_result.QuizResultContract.QuizResultEvent
import com.kakao.presentation.ui.screen.quiz_result.QuizResultContract.QuizResultSideEffect
import com.kakao.presentation.ui.screen.quiz_result.QuizResultContract.QuizResultState
import com.kakao.presentation.ui.view.ButtonColor
import com.kakao.presentation.ui.view.GButton
import com.kakao.presentation.ui.view.Gap
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
fun QuizResultRoute(
  navigateToHome: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: QuizResultViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val quizResultState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is QuizResultSideEffect.NavigateToHome -> navigateToHome()
        is QuizResultSideEffect.ShowSnackbar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  if (isLoading) LoadingDialog()
  else QuizResultScreen(
    state = quizResultState,
    modifier = modifier,
    onClickHomeButton = { viewModel.sendEvent(QuizResultEvent.OnClickHomeButton) }
  )
}

@Composable
private fun QuizResultScreen(
  state: QuizResultState,
  modifier: Modifier = Modifier,
  onClickHomeButton: () -> Unit = {},
) {
  Screen(
    modifier = modifier.padding(32.dp)
  ) {
    val isSuccess = state.score >= SCORE_STANDARD

    if (isSuccess) Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.End
    ) { PenCountView(state.pen) }
    else Gap(height = 56.dp)

    Gap(minHeight = 16.dp)
    QuizResultContent(
      score = state.score,
      isSuccess = isSuccess
    )
    Gap(minHeight = 16.dp)
    GButton(
      text = stringResource(R.string.quiz_result_home_button),
      onClick = onClickHomeButton,
      colors = ButtonColor.Tertiary,
      modifier = Modifier.width(128.dp)
    )
  }
}

@Composable
private fun QuizResultContent(
  score: Int,
  isSuccess: Boolean,
  modifier: Modifier = Modifier,
  windowSize: WindowSize = LocalWindowSize.current
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(72.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    QuizResultTitle(
      score = score,
      isSuccess = isSuccess
    )
    if (windowSize > WindowHeightSizeClass.Compact) QuizResultImage(isSuccess)
    Text(
      stringResource(if (isSuccess) R.string.quiz_result_body_success else R.string.quiz_result_body_failure),
      style = MaterialTheme.typography.bodyLarge
    )
  }
}

@Composable
private fun QuizResultTitle(
  score: Int,
  isSuccess: Boolean,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(22.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      stringResource(if (isSuccess) R.string.quiz_result_title_success else R.string.quiz_result_title_failure),
      style = MaterialTheme.typography.headlineMedium
    )
    Text(
      stringResource(R.string.quiz_result_subtitle, QuizConfig.ROUNDS, score),
      style = MaterialTheme.typography.titleLarge
    )
  }
}

@Composable
private fun QuizResultImage(
  isSuccess: Boolean,
  modifier: Modifier = Modifier
) {
  if (isSuccess) {
    Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterHorizontally),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        painterResource(R.drawable.img_home_pen),
        contentDescription = "pen",
        modifier = Modifier.height(100.dp)
      )
      Text(
        stringResource(R.string.quiz_result_pen_success),
        style = MaterialTheme.typography.titleLarge
      )
    }
  } else {
    Box(
      modifier = modifier,
      contentAlignment = Alignment.Center
    ) {
      Image(
        painterResource(R.drawable.img_crying_emoji),
        contentDescription = "Crying Emoji",
        modifier = Modifier.size(100.dp)
      )
    }
  }
}

@PhonePortraitPreview
@Composable
private fun QuizResultScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    QuizResultScreen(
      QuizResultState(
        score = 8,
        pen = 5
      )
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun QuizResultScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    QuizResultScreen(
      QuizResultState(
        score = 8,
        pen = 5
      )
    )
  }
}

@TabletPortraitPreview
@Composable
private fun QuizResultScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    QuizResultScreen(
      QuizResultState(
        score = 8,
        pen = 5
      )
    )
  }
}

@TabletLandscapePreview
@Composable
private fun QuizResultScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    QuizResultScreen(
      QuizResultState(
        score = 8,
        pen = 5
      )
    )
  }
}

@PhonePortraitPreview
@Composable
private fun QuizResultScreenPhonePortraitPreview2() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    QuizResultScreen(
      QuizResultState(score = 3)
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun QuizResultScreenPhoneLandscapePreview2() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    QuizResultScreen(
      QuizResultState(score = 3)
    )
  }
}

@TabletPortraitPreview
@Composable
private fun QuizResultScreenTabletPortraitPreview2() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    QuizResultScreen(
      QuizResultState(score = 3)
    )
  }
}

@TabletLandscapePreview
@Composable
private fun QuizResultScreenTabletLandscapePreview2() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    QuizResultScreen(
      QuizResultState(score = 3)
    )
  }
}