package com.kakao.presentation.ui.screen.quiz_ai_robby

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.presentation.R
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.ui.screen.quiz_ai_robby.QuizAIRobbyContract.QuizAIRobbyEvent
import com.kakao.presentation.ui.screen.quiz_ai_robby.QuizAIRobbyContract.QuizAIRobbySideEffect
import com.kakao.presentation.ui.screen.quiz_ai_robby.QuizAIRobbyContract.QuizAIRobbyState
import com.kakao.presentation.ui.view.AutoScrollingCarousel
import com.kakao.presentation.ui.view.Gap
import com.kakao.presentation.ui.view.LargeButton
import com.kakao.presentation.ui.view.LoadingDialog
import com.kakao.presentation.ui.view.Screen
import com.kakao.presentation.utility.LocalWindowSize
import com.kakao.presentation.utility.PhoneLandscapePreview
import com.kakao.presentation.utility.PhonePortraitPreview
import com.kakao.presentation.utility.ScreenPreview
import com.kakao.presentation.utility.TabletLandscapePreview
import com.kakao.presentation.utility.TabletPortraitPreview
import com.kakao.presentation.utility.WindowSize
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuizAIRobbyRoute(
  popBackStack: () -> Unit,
  navigateToQuizAI: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: QuizAIRobbyViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val quizAIState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is QuizAIRobbySideEffect.PopBackStack -> popBackStack()
        is QuizAIRobbySideEffect.NavigateToQuizAI -> navigateToQuizAI()
        is QuizAIRobbySideEffect.ShowSnackbar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  if (isLoading) LoadingDialog()
  else QuizAIRobbyScreen(
    state = quizAIState,
    modifier = modifier,
    onClickBackButton = { viewModel.sendEvent(QuizAIRobbyEvent.OnClickBackButton) },
    onClickStartButton = { viewModel.sendEvent(QuizAIRobbyEvent.OnClickStartButton) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizAIRobbyScreen(
  state: QuizAIRobbyState,
  modifier: Modifier = Modifier,
  onClickBackButton: () -> Unit = {},
  onClickStartButton: () -> Unit = {},
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
    QuizAIRobbyTitle()
    Gap(minHeight = 16.dp)
    ImageCarousel(state.imageList)
    Gap(minHeight = 16.dp)
    Text(
      stringResource(R.string.quiz_ai_robby_description_2),
      style = MaterialTheme.typography.bodyLarge
    )
    Gap(height = 28.dp)
    LargeButton(
      text = stringResource(R.string.quiz_ai_robby_start_button),
      modifier = Modifier.padding(horizontal = 32.dp),
      onClick = onClickStartButton
    )
    Gap(height = if (windowSize <= WindowHeightSizeClass.Compact) 32.dp else 64.dp)
  }
}

@Composable
private fun QuizAIRobbyTitle(
  modifier: Modifier = Modifier,
  windowSize: WindowSize = LocalWindowSize.current
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      stringResource(R.string.quiz_ai_robby_title),
      color = MaterialTheme.colorScheme.primary,
      style = if (windowSize <= WindowWidthSizeClass.Compact) MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        else MaterialTheme.typography.displaySmall
    )
    Text(
      stringResource(R.string.quiz_ai_robby_description_1),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleMedium
    )
  }
}

@Composable
private fun ImageCarousel(
  imageList: List<ImageUIModel>,
  modifier: Modifier = Modifier
) {
  if (imageList.isNotEmpty()) {
    AutoScrollingCarousel(
      itemList = imageList,
      modifier = modifier,
      horizontalPadding = 24.dp
    ) { image ->
      CoilImage(
        imageModel = { image.url },
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .height(160.dp)
          .border(2.dp, MaterialTheme.colorScheme.outline),
        imageOptions = ImageOptions(
          contentScale = ContentScale.Fit
        ),
        previewPlaceholder = R.drawable.img_home_picture
      )
    }
  }
}

@PhonePortraitPreview
@Composable
private fun QuizAIRobbyScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    QuizAIRobbyScreen(
      QuizAIRobbyState(
        imageList = List(5) {
          ImageUIModel()
        }
      )
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun QuizAIRobbyScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    QuizAIRobbyScreen(
      QuizAIRobbyState(
        imageList = List(5) {
          ImageUIModel()
        }
      )
    )
  }
}

@TabletPortraitPreview
@Composable
private fun QuizAIRobbyScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    QuizAIRobbyScreen(
      QuizAIRobbyState(
        imageList = List(5) {
          ImageUIModel()
        }
      )
    )
  }
}

@TabletLandscapePreview
@Composable
private fun QuizAIRobbyScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    QuizAIRobbyScreen(
      QuizAIRobbyState(
        imageList = List(5) {
          ImageUIModel()
        }
      )
    )
  }
}