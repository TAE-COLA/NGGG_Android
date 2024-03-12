package com.kakao.presentation.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.kakao.presentation.R
import com.kakao.presentation.ui.screen.MainActivity
import com.kakao.presentation.ui.screen.drawing_ai.DrawingAIRoute
import com.kakao.presentation.ui.screen.drawing_ai_result.DrawingAIResultRoute
import com.kakao.presentation.ui.screen.drawing_robby.DrawingRobbyRoute
import com.kakao.presentation.ui.screen.drawing_user.DrawingUserRoute
import com.kakao.presentation.ui.screen.drawing_user_result.DrawingUserResultRoute
import com.kakao.presentation.ui.screen.drawing_user_sketch.DrawingUserSketchRoute
import com.kakao.presentation.ui.screen.home.HomeRoute
import com.kakao.presentation.ui.screen.login.LoginRoute
import com.kakao.presentation.ui.screen.my_page.MyPageRoute
import com.kakao.presentation.ui.screen.quiz_ai.QuizAIRoute
import com.kakao.presentation.ui.screen.quiz_ai_robby.QuizAIRobbyRoute
import com.kakao.presentation.ui.screen.quiz_friend.QuizFriendRoute
import com.kakao.presentation.ui.screen.quiz_history.QuizHistoryActivity
import com.kakao.presentation.ui.screen.quiz_result.QuizResultRoute
import com.kakao.presentation.ui.screen.quiz_user.QuizUserRoute
import com.kakao.presentation.ui.screen.quiz_user_robby.QuizUserRobbyRoute
import com.kakao.presentation.ui.view.GDialog
import com.kakao.presentation.utility.ConnectionManager
import com.kakao.presentation.utility.toJson

@Composable
fun MainNavHost(
  startDestination: String,
  connectionManager: ConnectionManager,
  showSnackBar: suspend (String) -> Unit,
  navController: NavHostController,
  modifier: Modifier = Modifier,
  context: Context = LocalContext.current
) {
  fun NavHostController.checkNetwork() =
    if (connectionManager.currentState == ConnectionManager.LOST)
      navigate(MainNavRoute.NetworkLostDialog.route)
    else
      Unit

  fun NavHostController.navigate(route: MainNavRoute, vararg arguments: Any?) {
    navigate(route.routeWithArgs(arguments.asList()))
    checkNetwork()
  }

  fun NavHostController.navigateWithPop(route: MainNavRoute, vararg arguments: Any?) {
    navigate(route.routeWithArgs(arguments.asList())) { popBackStack() }
    checkNetwork()
  }

  fun NavHostController.popBackStack(destinationRoute: MainNavRoute) {
    popBackStack(destinationRoute.name, false)
    checkNetwork()
  }

  fun NavGraphBuilder.composable(
    route: MainNavRoute,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
  ) = composable(
    route = route.route,
    arguments = route.arguments,
    deepLinks = route.deepLinks,
    content = content
  )

  fun NavGraphBuilder.dialog(
    route: MainNavRoute,
    dialogProperties: DialogProperties = DialogProperties(),
    content: @Composable (NavBackStackEntry) -> Unit
  ) = dialog(
    route = route.route,
    arguments = route.arguments,
    deepLinks = route.deepLinks,
    dialogProperties = dialogProperties,
    content = content
  )

  val connectionState by connectionManager.connectionState.collectAsStateWithLifecycle()
  LaunchedEffect(connectionState) {
    navController.checkNetwork()
  }

  NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier
  ) {

    composable(MainNavRoute.Login) {
      LoginRoute(
        navigateToHome = { navController.navigateWithPop(MainNavRoute.Home) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.Home) {
      HomeRoute(
        navigateToQuizAIRobby = { navController.navigate(MainNavRoute.QuizAIRobby) },
        navigateToQuizUserRobby = { navController.navigate(MainNavRoute.QuizUserRobby) },
        navigateToDrawingRobby = { navController.navigate(MainNavRoute.DrawingRobby) },
        navigateToQuizHistory = {
          val intent = Intent(context as MainActivity, QuizHistoryActivity::class.java)
          context.startActivity(intent) 
        },
        navigateToQuizFriend = { imageId ->
          navController.navigate(MainNavRoute.QuizFriend, imageId)
        },
        navigateToMyPage = { navController.navigate(MainNavRoute.MyPage) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.QuizAIRobby) {
      QuizAIRobbyRoute(
        popBackStack = { navController.popBackStack() },
        navigateToQuizAI = { navController.navigate(MainNavRoute.QuizAI) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.QuizAI) {
      QuizAIRoute(
        navigateToHome = { navController.popBackStack(MainNavRoute.Home) },
        navigateToQuizResult = { score ->
          navController.navigateWithPop(MainNavRoute.QuizResult, score)
        },
        openExitDialog = { navController.navigate(MainNavRoute.QuizExitDialog) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.QuizUserRobby) {
      QuizUserRobbyRoute(
        popBackStack = { navController.popBackStack() },
        navigateToQuizUser = { navController.navigate(MainNavRoute.QuizUser) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.QuizUser) {
      QuizUserRoute(
        navigateToHome = { navController.popBackStack(MainNavRoute.Home) },
        navigateToQuizResult = { score ->
          navController.navigateWithPop(MainNavRoute.QuizResult, score)
        },
        openExitDialog = { navController.navigate(MainNavRoute.QuizExitDialog) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.QuizResult) {
      QuizResultRoute(
        navigateToHome = { navController.popBackStack(MainNavRoute.Home) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.DrawingRobby) {
      DrawingRobbyRoute(
        popBackStack = { navController.popBackStack() },
        navigateToDrawingAI = { navController.navigate(MainNavRoute.DrawingAI) },
        navigateToDrawingUser = { navController.navigate(MainNavRoute.DrawingUser) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.DrawingAI) {
      DrawingAIRoute(
        popBackStack = { navController.popBackStack() },
        navigateToDrawingAIResult = { keyword ->
          navController.navigateWithPop(MainNavRoute.DrawingAIResult, keyword.toJson())
        },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.DrawingAIResult) {
      DrawingAIResultRoute(
        navigateToHome = { navController.popBackStack(MainNavRoute.Home) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.DrawingUser) {
      DrawingUserRoute(
        popBackStack = { navController.popBackStack() },
        navigateToDrawingUserSketch = { keyword ->
          navController.navigate(MainNavRoute.DrawingUserSketch, keyword.toJson())
        },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.DrawingUserSketch) {
      DrawingUserSketchRoute(
        navigateToDrawingUserResult = { keyword ->
          navController.navigateWithPop(MainNavRoute.DrawingUserResult, keyword.toJson())
        },
        popBackStack = { navController.popBackStack() },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.DrawingUserResult) {
      DrawingUserResultRoute(
        navigateToHome = { navController.popBackStack(MainNavRoute.Home) },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.MyPage) {
      MyPageRoute(
        popBackStack = { navController.popBackStack() },
        navigateToLogin = {
          navController.popBackStack()
          navController.navigateWithPop(MainNavRoute.Login)
        },
        showSnackbar = showSnackBar
      )
    }
    composable(MainNavRoute.QuizFriend) {
      QuizFriendRoute(
        navigateToHome = { navController.popBackStack(MainNavRoute.Home) },
        openExitDialog = { navController.navigate(MainNavRoute.QuizExitDialog) },
        showSnackbar = showSnackBar
      )
    }

    dialog(MainNavRoute.QuizExitDialog) {
      GDialog(
        title = stringResource(R.string.quiz_exit_dialog_title),
        body = stringResource(R.string.quiz_exit_dialog_body),
        dismissButtonText = stringResource(R.string.quiz_exit_dialog_dismiss),
        onDismissRequest = { navController.popBackStack() },
        confirmButtonText = stringResource(R.string.quiz_exit_dialog_confirm),
        onConfirmRequest = { navController.popBackStack(MainNavRoute.Home) }
      )
    }
    dialog(
      MainNavRoute.NetworkLostDialog,
      dialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
      )
    ) {
      GDialog(
        title = stringResource(R.string.network_lost_dialog_title),
        body = stringResource(R.string.network_lost_dialog_body),
        dismissButtonText = stringResource(R.string.network_lost_dialog_dismiss),
        onDismissRequest = { (context as MainActivity).finish() },
        confirmButtonText = stringResource(R.string.network_lost_dialog_confirm),
        onConfirmRequest = {
          navController.popBackStack()
          navController.checkNetwork()
        }
      )
    }
  }
}