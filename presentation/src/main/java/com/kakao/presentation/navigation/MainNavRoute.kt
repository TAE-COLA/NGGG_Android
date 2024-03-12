package com.kakao.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.kakao.presentation.BuildConfig
import com.kakao.presentation.model.KeywordUIModelArgType

enum class MainNavRoute(
  val args: List<Pair<String, NavType<*>>> = emptyList(),
  val links: List<String> = emptyList(),
) {
  Login,
  Home(links = listOf("kakao${BuildConfig.KAKAO_API_KEY}://kakaolink?imageId={imageId}")),

  QuizAIRobby,
  QuizAI,

  QuizUserRobby,
  QuizUser,
  QuizResult(args = listOf("score" to NavType.IntType)),

  DrawingRobby,

  DrawingAI,
  DrawingAIResult(args = listOf("keyword" to KeywordUIModelArgType())),

  DrawingUser,
  DrawingUserSketch(args = listOf("keyword" to KeywordUIModelArgType())),
  DrawingUserResult(args = listOf("keyword" to KeywordUIModelArgType())),

  MyPage,

  QuizFriend(args = listOf("imageId" to NavType.StringType)),

  QuizExitDialog,
  NetworkLostDialog
}

internal val MainNavRoute.route: String
  get() = this.name + this.args.joinToString { "?${it.first}={${it.first}}" }

internal fun MainNavRoute.routeWithArgs(arguments: List<Any?>): String =
  this.name + this.args.zip(arguments) { key, value -> "?${key.first}=$value" }.joinToString()

internal val MainNavRoute.arguments: List<NamedNavArgument>
  get() = this.args.map { (key, navType) -> navArgument(key) { type = navType } }

internal val MainNavRoute.deepLinks: List<NavDeepLink>
  get() = this.links.map { navDeepLink { uriPattern = it } }