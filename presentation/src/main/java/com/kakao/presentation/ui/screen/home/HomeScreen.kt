package com.kakao.presentation.ui.screen.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.presentation.R
import com.kakao.presentation.ui.screen.home.HomeContract.HomeEvent
import com.kakao.presentation.ui.screen.home.HomeContract.HomeSideEffect
import com.kakao.presentation.ui.view.Gap
import com.kakao.presentation.ui.view.LargeButton
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
fun HomeRoute(
  navigateToQuizAIRobby: () -> Unit,
  navigateToQuizUserRobby: () -> Unit,
  navigateToDrawingRobby: () -> Unit,
  navigateToQuizHistory: () -> Unit,
  navigateToMyPage: () -> Unit,
  navigateToQuizFriend: (String) -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is HomeSideEffect.NavigateToQuizAIRobby -> navigateToQuizAIRobby()
        is HomeSideEffect.NavigateToQuizUserRobby -> navigateToQuizUserRobby()
        is HomeSideEffect.NavigateToDrawingRobby -> navigateToDrawingRobby()
        is HomeSideEffect.NavigateToQuizHistory -> navigateToQuizHistory()
        is HomeSideEffect.NavigateToMyPage -> navigateToMyPage()
        is HomeSideEffect.NavigateToQuizFriend -> navigateToQuizFriend(it.imageId)
        is HomeSideEffect.ShowSnackBar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  HomeScreen(
    modifier = modifier,
    onClickQuizAIRobbyButton = { viewModel.sendEvent(HomeEvent.OnClickQuizAIRobbyButton) },
    onClickQuizUserRobbyButton = { viewModel.sendEvent(HomeEvent.OnClickQuizUserRobbyButton) },
    onClickDrawingRobbyButton = { viewModel.sendEvent(HomeEvent.OnClickDrawingRobbyButton) },
    onClickQuizHistoryButton = { viewModel.sendEvent(HomeEvent.OnClickQuizHistoryButton) },
    onClickMyPageButton = { viewModel.sendEvent(HomeEvent.OnClickMyPageButton) }
  )
}

@Composable
private fun HomeScreen(
  modifier: Modifier = Modifier,
  onClickQuizAIRobbyButton: () -> Unit = {},
  onClickQuizUserRobbyButton: () -> Unit = {},
  onClickDrawingRobbyButton: () -> Unit = {},
  onClickQuizHistoryButton: () -> Unit = {},
  onClickMyPageButton: () -> Unit = {},
  windowSize: WindowSize = LocalWindowSize.current
) {
  Screen(
    modifier = modifier.padding(start = 32.dp, top = if (windowSize < WindowHeightSizeClass.Medium) 32.dp else 64.dp, end = 32.dp, bottom = 32.dp),
  ) {
    HomeTitle(
      modifier = Modifier.fillMaxWidth()
    )
    Gap(minHeight = 16.dp)
    HomeButtons(
      onClickQuizAIRobbyButton = onClickQuizAIRobbyButton,
      onClickQuizUserRobbyButton = onClickQuizUserRobbyButton,
      onClickDrawingRobbyButton = onClickDrawingRobbyButton,
      onClickQuizHistoryButton = onClickQuizHistoryButton
    )
    Gap(height = 24.dp)
    HomeIconButton(
      onClickMyPageButton = onClickMyPageButton,
      modifier = Modifier.fillMaxWidth()
    )
  }
}

@Composable
private fun HomeTitle(
  modifier: Modifier = Modifier,
  windowSize: WindowSize = LocalWindowSize.current
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(
      buildAnnotatedString {
        val titleSpanStyle = if (windowSize <= WindowWidthSizeClass.Compact) MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold).toSpanStyle()
          else MaterialTheme.typography.displaySmall.toSpanStyle()
        val titleEmphasizedSpanStyle = if (windowSize <= WindowWidthSizeClass.Compact) MaterialTheme.typography.displaySmall.toSpanStyle()
          else MaterialTheme.typography.displayMedium.toSpanStyle()

        withStyle(titleSpanStyle) {
          append(stringResource(R.string.home_title_1))
        }
        withStyle(titleEmphasizedSpanStyle) {
          append(stringResource(R.string.home_title_2))
        }
        withStyle(titleSpanStyle) {
          append(stringResource(R.string.home_title_3))
        }
      }
    )
    Text(
      stringResource(R.string.home_description),
      style = MaterialTheme.typography.bodyLarge
    )
  }
}

@Composable
private fun HomeButtons(
  onClickQuizAIRobbyButton: () -> Unit,
  onClickQuizUserRobbyButton: () -> Unit,
  onClickDrawingRobbyButton: () -> Unit,
  onClickQuizHistoryButton: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    LargeButton(
      stringResource(R.string.home_quiz_ai_robby_button),
      onClick = onClickQuizAIRobbyButton
    )
    LargeButton(
      stringResource(R.string.home_quiz_user_robby_button),
      onClick = onClickQuizUserRobbyButton
    )
    LargeButton(
      stringResource(R.string.home_drawing_robby_button),
      onClick = onClickDrawingRobbyButton
    )
    LargeButton(
      stringResource(R.string.home_quiz_history_button),
      onClick = onClickQuizHistoryButton
    )
  }
}

@Composable
fun HomeIconButton(
  onClickMyPageButton: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.End
  ) {
    ElevatedButton(
      onClick = onClickMyPageButton,
      shape = IconButtonDefaults.filledShape,
      colors = ButtonDefaults.elevatedButtonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
      ),
      elevation = ButtonDefaults.buttonElevation(
        defaultElevation = 6.dp
      )
    ) {
      Icon(
        Icons.Rounded.Settings,
        contentDescription = "My Page"
      )
    }
  }
}

@PhonePortraitPreview
@Composable
private fun HomeScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    HomeScreen()
  }
}

@PhoneLandscapePreview
@Composable
private fun HomeScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    HomeScreen()
  }
}

@TabletPortraitPreview
@Composable
private fun HomeScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    HomeScreen()
  }
}

@TabletLandscapePreview
@Composable
private fun HomeScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    HomeScreen()
  }
}