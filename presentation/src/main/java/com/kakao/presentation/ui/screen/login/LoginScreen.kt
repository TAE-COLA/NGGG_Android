package com.kakao.presentation.ui.screen.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.presentation.R
import com.kakao.presentation.ui.screen.login.LoginContract.LoginEvent
import com.kakao.presentation.ui.screen.login.LoginContract.LoginSideEffect
import com.kakao.presentation.ui.view.Gap
import com.kakao.presentation.ui.view.KakaoLoginButton
import com.kakao.presentation.ui.view.KakaoLoginButtonSize
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
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
  navigateToHome: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: LoginViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is LoginSideEffect.NavigateToHome -> navigateToHome()
        is LoginSideEffect.ShowSnackBar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  if (isLoading) LoadingScreen()
  else LoginScreen(
    modifier = modifier,
    onClickLoginButton = { viewModel.sendEvent(LoginEvent.OnClickLoginButton(context)) }
  )
}

@Composable
fun LoginScreen(
  modifier: Modifier = Modifier,
  onClickLoginButton: () -> Unit = {}
) {
  Screen(
    modifier = modifier.padding(horizontal = 32.dp, vertical = 64.dp)
  ) {
    LoginTitle(
      modifier = Modifier.fillAppropriateWidth()
    )
    Gap()
    Text(
      stringResource(R.string.login_karlo_text),
      style = MaterialTheme.typography.labelMedium
    )
    Gap(height = 8.dp)
    KakaoLoginButton(
      onClick = onClickLoginButton,
      modifier = Modifier.fillAppropriateWidth(),
      size = KakaoLoginButtonSize.Medium
    )
  }
}

@Composable
private fun LoginTitle(
  modifier: Modifier = Modifier,
  windowSize: WindowSize = LocalWindowSize.current
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    if (windowSize > WindowHeightSizeClass.Compact) {
      Image(
        painterResource(R.drawable.img_giraffe),
        contentDescription = "Giraffe",
        modifier = Modifier
          .padding(
            start = if (windowSize <= WindowWidthSizeClass.Compact) 80.dp else 110.dp
          )
          .size(152.dp, 72.dp)
      )
    }
    Text(
      buildAnnotatedString {
        val titleSpanStyle = if (windowSize <= WindowWidthSizeClass.Compact) MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold).toSpanStyle()
          else MaterialTheme.typography.displaySmall.toSpanStyle()
        val titleEmphasizedSpanStyle = if (windowSize <= WindowWidthSizeClass.Compact) MaterialTheme.typography.displaySmall.toSpanStyle()
          else MaterialTheme.typography.displayMedium.toSpanStyle()

        withStyle(titleSpanStyle) {
          append(stringResource(R.string.login_title_1))
        }
        withStyle(titleEmphasizedSpanStyle) {
          append(stringResource(R.string.login_title_2))
        }
        withStyle(titleSpanStyle) {
          append(stringResource(R.string.login_title_3))
        }
      }
    )
    Text(
      stringResource(id = R.string.login_sub_title),
      modifier = Modifier.padding(top = 8.dp),
      style = MaterialTheme.typography.bodyLarge
    )
  }
}

@PhonePortraitPreview
@Composable
private fun LoginScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    LoginScreen()
  }
}

@PhoneLandscapePreview
@Composable
private fun LoginScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    LoginScreen()
  }
}

@TabletPortraitPreview
@Composable
private fun LoginScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    LoginScreen()
  }
}

@TabletLandscapePreview
@Composable
private fun LoginScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    LoginScreen()
  }
}