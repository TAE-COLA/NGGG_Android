package com.kakao.presentation.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.kakao.domain.usecase.CheckUserInfo
import com.kakao.presentation.navigation.MainNavHost
import com.kakao.presentation.navigation.MainNavRoute
import com.kakao.presentation.navigation.route
import com.kakao.presentation.ui.theme.NGGGTheme
import com.kakao.presentation.ui.theme.SoundTheme
import com.kakao.presentation.utility.ConnectionManager
import com.kakao.presentation.utility.LocalWindowSize
import com.kakao.presentation.utility.WindowSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @Inject
  lateinit var checkUserInfo: CheckUserInfo

  @Inject
  lateinit var connectionManager: ConnectionManager

  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val windowSizeClass = calculateWindowSizeClass(this)
      val snackbarHostState = remember { SnackbarHostState() }
      val navController = rememberNavController()

      CompositionLocalProvider(
        LocalWindowSize provides WindowSize(windowSizeClass)
      ) {
        SoundTheme {
          NGGGTheme {
            Surface(
              modifier = Modifier.fillMaxSize()
            ) {
              Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
              ) { padding ->
                val snackbarChannel: Channel<String> = Channel()
                LaunchedEffect(snackbarChannel) {
                  snackbarChannel.receiveAsFlow().collectLatest { message ->
                    snackbarHostState.showSnackbar(message)
                  }
                }

                MainNavHost(
                  startDestination = if (checkUserInfo()) MainNavRoute.Home.route else MainNavRoute.Login.route,
                  connectionManager = connectionManager,
                  showSnackBar = { snackbarChannel.send(it) },
                  navController = navController,
                  modifier = Modifier.padding(padding)
                )
              }
            }
          }
        }
      }
    }
  }
}