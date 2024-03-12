package com.kakao.presentation.ui.screen.my_page

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.kakao.presentation.ui.screen.my_page.MyPageContract.MyPageEvent
import com.kakao.presentation.ui.screen.my_page.MyPageContract.MyPageSideEffect
import com.kakao.presentation.ui.screen.my_page.MyPageContract.MyPageState
import com.kakao.presentation.ui.view.ButtonColor
import com.kakao.presentation.ui.view.GButton
import com.kakao.presentation.ui.view.Gap
import com.kakao.presentation.ui.view.LoadingScreen
import com.kakao.presentation.ui.view.PenCountView
import com.kakao.presentation.ui.view.Screen
import com.kakao.presentation.utility.PhoneLandscapePreview
import com.kakao.presentation.utility.PhonePortraitPreview
import com.kakao.presentation.utility.ScreenPreview
import com.kakao.presentation.utility.TabletLandscapePreview
import com.kakao.presentation.utility.TabletPortraitPreview
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyPageRoute(
  popBackStack: () -> Unit,
  navigateToLogin: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: MyPageViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val myPageState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is MyPageSideEffect.PopBackStack -> popBackStack()
        is MyPageSideEffect.NavigateToLogin -> navigateToLogin()
        is MyPageSideEffect.ShowSnackbar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  if (isLoading) LoadingScreen()
  else MyPageScreen(
    state = myPageState,
    modifier = modifier,
    onClickBackButton = { viewModel.sendEvent(MyPageEvent.OnClickBackButton) },
    onClickLogoutButton = { viewModel.sendEvent(MyPageEvent.OnClickLogoutButton) },
    onClickSignOutButton = { viewModel.sendEvent(MyPageEvent.OnClickSignOutButton) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
  state: MyPageState,
  modifier: Modifier = Modifier,
  onClickBackButton: () -> Unit = {},
  onClickLogoutButton: () -> Unit = {},
  onClickSignOutButton: () -> Unit = {}
) {
  Screen(
    modifier = modifier,
    topAppBar = {
      CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.my_page_title)) },
        navigationIcon = {
          IconButton(onClick = onClickBackButton) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp),
      horizontalArrangement = Arrangement.End
    ) { PenCountView(state.user.pen) }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp),
      horizontalArrangement = Arrangement.spacedBy(24.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        stringResource(R.string.my_page_name),
        style = MaterialTheme.typography.titleMedium
      )
      Text(state.user.name)
    }
    Gap()
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(32.dp),
      horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
      GButton(
        text = stringResource(R.string.my_page_logout),
        onClick = onClickLogoutButton,
        modifier = Modifier.weight(1f),
        colors = ButtonColor.Tertiary
      )
      GButton(
        text = stringResource(R.string.my_page_sign_out),
        onClick = onClickSignOutButton,
        modifier = Modifier.weight(1f),
        colors = ButtonColor.Error
      )
    }
  }
}

@PhonePortraitPreview
@Composable
private fun MyPageScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    MyPageScreen(
      MyPageState()
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun MyPageScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    MyPageScreen(
      MyPageState()
    )
  }
}

@TabletPortraitPreview
@Composable
private fun MyPageScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    MyPageScreen(
      MyPageState()
    )
  }
}

@TabletLandscapePreview
@Composable
private fun MyPageScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    MyPageScreen(
      MyPageState()
    )
  }
}