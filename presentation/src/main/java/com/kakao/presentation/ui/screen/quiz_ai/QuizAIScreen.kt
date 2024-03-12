package com.kakao.presentation.ui.screen.quiz_ai

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.core.QuizConfig
import com.kakao.presentation.R
import com.kakao.presentation.model.GeneratedImageUIModel
import com.kakao.presentation.model.KeywordUIModel
import com.kakao.presentation.ui.screen.quiz_ai.QuizAIContract.QuizAIEvent
import com.kakao.presentation.ui.screen.quiz_ai.QuizAIContract.QuizAISideEffect
import com.kakao.presentation.ui.screen.quiz_ai.QuizAIContract.QuizAIState
import com.kakao.presentation.ui.theme.SoundTheme
import com.kakao.presentation.ui.view.AnswerHint
import com.kakao.presentation.ui.view.BorderCoilImage
import com.kakao.presentation.ui.view.ButtonColor
import com.kakao.presentation.ui.view.GButton
import com.kakao.presentation.ui.view.GTextField
import com.kakao.presentation.ui.view.Gap
import com.kakao.presentation.ui.view.LoadingDialog
import com.kakao.presentation.ui.view.LoadingScreen
import com.kakao.presentation.ui.view.QuizIndicator
import com.kakao.presentation.ui.view.Screen
import com.kakao.presentation.ui.view.Shaker
import com.kakao.presentation.utility.DefaultValues
import com.kakao.presentation.utility.LinearTimer
import com.kakao.presentation.utility.LocalWindowSize
import com.kakao.presentation.utility.PhoneLandscapePreview
import com.kakao.presentation.utility.PhonePortraitPreview
import com.kakao.presentation.utility.ScreenPreview
import com.kakao.presentation.utility.ShakeConfig
import com.kakao.presentation.utility.TabletLandscapePreview
import com.kakao.presentation.utility.TabletPortraitPreview
import com.kakao.presentation.utility.WindowSize
import com.kakao.presentation.utility.fillAppropriateWidth
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuizAIRoute(
  navigateToHome: () -> Unit,
  navigateToQuizResult: (Int) -> Unit,
  openExitDialog: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: QuizAIViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val quizAIState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is QuizAISideEffect.NavigateToHome -> navigateToHome()
        is QuizAISideEffect.NavigateToQuizResult -> navigateToQuizResult(quizAIState.score.count { isCorrect -> isCorrect == true })
        is QuizAISideEffect.OpenExitDialog -> openExitDialog()
        is QuizAISideEffect.ShowSnackbar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  BackHandler {
    viewModel.sendEvent(QuizAIEvent.OnClickCloseButton)
  }

  if (quizAIState.currentRound >= quizAIState.imageList.size) LoadingDialog(
    text = stringResource(R.string.quiz_ai_loading)
  )
  else if (isLoading) LoadingScreen(
    message = stringResource(R.string.quiz_ai_save_loading)
  )
  else QuizAIScreen(
    state = quizAIState,
    modifier = modifier,
    onClickCloseButton = { viewModel.sendEvent(QuizAIEvent.OnClickCloseButton) },
    onChangeAnswer = { viewModel.sendEvent(QuizAIEvent.OnChangeAnswer(it)) },
    onClickSubmitButton = { viewModel.sendEvent(QuizAIEvent.OnClickSubmitButton) },
    onTimeout = { viewModel.sendEvent(QuizAIEvent.OnTimeout) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizAIScreen(
  state: QuizAIState,
  modifier: Modifier = Modifier,
  onClickCloseButton: () -> Unit = {},
  onChangeAnswer: (String) -> Unit = {},
  onClickSubmitButton: () -> Unit = {},
  onTimeout: () -> Unit = {},
  windowSize: WindowSize = LocalWindowSize.current
) {
  Screen(
    modifier = modifier,
    topAppBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.quiz_ai_title)) },
        navigationIcon = {
          IconButton(onClick = onClickCloseButton) {
            Icon(Icons.Outlined.Close, contentDescription = "Close")
          }
        }
      )
      LinearTimer(
        seconds = QuizConfig.LIMIT_TIME,
        onTimeout = onTimeout,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp)
      )
    }
  ) {
    if (windowSize > WindowHeightSizeClass.Compact || windowSize <= WindowWidthSizeClass.Compact) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 32.dp, top = 0.dp, end = 32.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        IndicatorAndImage(
          score = state.score,
          imageUrl = state.imageList[state.currentRound].url,
          modifier = Modifier.fillAppropriateWidth()
        )
        HintAndAnswerField(
          answers = state.imageList[state.currentRound].answer.map { it.korean },
          isCorrect = state.isCorrect,
          answer = state.answer,
          onChangeAnswer = onChangeAnswer,
          onClickSubmitButton = onClickSubmitButton,
          modifier = Modifier.fillAppropriateWidth()
        )
      }
    } else {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 32.dp, top = 0.dp, end = 32.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.Bottom
      ) {
        IndicatorAndImage(
          score = state.score,
          imageUrl = state.imageList[state.currentRound].url,
          modifier = Modifier
            .weight(1f)
            .widthIn(max = DefaultValues.APPROPRIATE_WIDTH_DP.dp)
        )
        HintAndAnswerField(
          answers = state.imageList[state.currentRound].answer.map { it.korean },
          isCorrect = state.isCorrect,
          answer = state.answer,
          onChangeAnswer = onChangeAnswer,
          onClickSubmitButton = onClickSubmitButton,
          modifier = Modifier
            .weight(1f)
            .widthIn(max = DefaultValues.APPROPRIATE_WIDTH_DP.dp),
        )
      }
    }
  }
}

@Composable
private fun IndicatorAndImage(
  score: List<Boolean?>,
  imageUrl: String,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    QuizIndicator(score = score)
    BorderCoilImage(url = imageUrl)
  }
}

@Composable
private fun HintAndAnswerField(
  answers: List<String>,
  isCorrect: Boolean?,
  answer: String,
  onChangeAnswer: (String) -> Unit,
  onClickSubmitButton: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    AnswerHint(
      answers = answers,
      hidden = isCorrect == null
    )
    AnimatedResultText(isCorrect)
    GTextField(
      value = answer,
      onValueChange = onChangeAnswer,
      modifier = Modifier.fillMaxWidth(),
      enabled = isCorrect == null,
      label = stringResource(R.string.quiz_ai_label)
    )
    GButton(
      text = when (isCorrect == null) {
        true -> stringResource(R.string.quiz_ai_submit_button_hidden)
        false -> stringResource(R.string.quiz_ai_submit_button_not_hidden)
      },
      onClick = onClickSubmitButton,
      modifier = Modifier.width(128.dp),
      enabled = answer.isNotBlank(),
      colors = ButtonColor.Secondary
    )
  }
}

@Composable
private fun ColumnScope.AnimatedResultText(
  isCorrect: Boolean?
) {
  if (isCorrect != null) {
    if (isCorrect) SoundTheme.soundScheme.correct.start()
    else SoundTheme.soundScheme.wrong.start()

    val shakeConfig = if (isCorrect) ShakeConfig(
      iterations = 4,
      intensity = 10_000f,
      rotateX = -20f,
      translateY = 20f,
    ) else ShakeConfig(
      iterations = 4,
      intensity = 10_000f,
      rotateY = 15f,
      translateX = 40f,
    )

    Shaker(
      shakeConfig = shakeConfig,
      trigger = true
    ) {
      Text(
        text = when (isCorrect) {
          true -> stringResource(R.string.quiz_ai_correct)
          false -> stringResource(R.string.quiz_ai_wrong)
        },
        color = when (isCorrect) {
          true -> MaterialTheme.colorScheme.primary
          false -> MaterialTheme.colorScheme.error
        },
        style = MaterialTheme.typography.titleLarge
      )
    }
  } else {
    Gap(height = 28.dp)
  }
}

@PhonePortraitPreview
@Composable
private fun QuizAIScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    QuizAIScreen(
      QuizAIState(
        imageList = List(QuizConfig.ROUNDS) {
          GeneratedImageUIModel(
            answer = listOf(KeywordUIModel("picture", "그림"))
          )
        }
      )
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun QuizAIScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    QuizAIScreen(
      QuizAIState(
        imageList = List(QuizConfig.ROUNDS) {
          GeneratedImageUIModel(
            answer = listOf(KeywordUIModel("picture", "그림"))
          )
        }
      )
    )
  }
}

@TabletPortraitPreview
@Composable
private fun QuizAIScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    QuizAIScreen(
      QuizAIState(
        imageList = List(QuizConfig.ROUNDS) {
          GeneratedImageUIModel(
            answer = listOf(KeywordUIModel("picture", "그림"))
          )
        }
      )
    )
  }
}

@TabletLandscapePreview
@Composable
private fun QuizAIScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    QuizAIScreen(
      QuizAIState(
        imageList = List(QuizConfig.ROUNDS) {
          GeneratedImageUIModel(
            answer = listOf(KeywordUIModel("picture", "그림"))
          )
        }
      )
    )
  }
}