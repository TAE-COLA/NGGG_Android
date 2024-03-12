package com.kakao.presentation.ui.screen.quiz_friend

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.presentation.R
import com.kakao.presentation.model.DrawingUIModel
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.ui.screen.quiz_friend.QuizFriendContract.QuizFriendEvent
import com.kakao.presentation.ui.screen.quiz_friend.QuizFriendContract.QuizFriendSideEffect
import com.kakao.presentation.ui.screen.quiz_friend.QuizFriendContract.QuizFriendState
import com.kakao.presentation.ui.view.AnswerHint
import com.kakao.presentation.ui.view.BorderCoilImage
import com.kakao.presentation.ui.view.ButtonColor
import com.kakao.presentation.ui.view.GButton
import com.kakao.presentation.ui.view.GTextField
import com.kakao.presentation.ui.view.LoadingDialog
import com.kakao.presentation.ui.view.PenCountView
import com.kakao.presentation.ui.view.Screen
import com.kakao.presentation.utility.DefaultValues
import com.kakao.presentation.utility.LocalWindowSize
import com.kakao.presentation.utility.PhoneLandscapePreview
import com.kakao.presentation.utility.PhonePortraitPreview
import com.kakao.presentation.utility.ScreenPreview
import com.kakao.presentation.utility.TabletLandscapePreview
import com.kakao.presentation.utility.TabletPortraitPreview
import com.kakao.presentation.utility.WindowSize
import com.kakao.presentation.utility.drawLineUIModel
import com.kakao.presentation.utility.fillAppropriateWidth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuizFriendRoute(
  navigateToHome: () -> Unit,
  openExitDialog: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: QuizFriendViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val quizFriendState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is QuizFriendSideEffect.NavigateToHome -> navigateToHome()
        is QuizFriendSideEffect.OpenExitDialog -> openExitDialog()
        is QuizFriendSideEffect.ShowSnackbar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  BackHandler {
    viewModel.sendEvent(QuizFriendEvent.OnClickCloseButton)
  }

  if (isLoading) LoadingDialog()
  else QuizFriendScreen(
    state = quizFriendState,
    modifier = modifier,
    onClickCloseButton = { viewModel.sendEvent(QuizFriendEvent.OnClickCloseButton) },
    onChangeAnswer = { viewModel.sendEvent(QuizFriendEvent.OnChangeAnswer(it)) },
    onClickSubmitButton = { viewModel.sendEvent(QuizFriendEvent.OnClickSubmitButton) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizFriendScreen(
  state: QuizFriendState,
  modifier: Modifier = Modifier,
  onClickCloseButton: () -> Unit = {},
  onChangeAnswer: (String) -> Unit = {},
  onClickSubmitButton: () -> Unit = {},
  windowSize: WindowSize = LocalWindowSize.current
) {
  Screen(
    modifier = modifier,
    topAppBar = {
      TopAppBar(
        title = {},
        navigationIcon = {
          IconButton(onClick = onClickCloseButton) {
            Icon(Icons.Outlined.Close, contentDescription = "Close")
          }
        }
      )
    }
  ) {
    QuizFriendTitleOrPen(
      isCorrect = state.isCorrect, 
      providerName = state.providerName,
      pen = state.user.pen,
      modifier = Modifier.fillMaxWidth()
    )
    if (windowSize > WindowHeightSizeClass.Compact || windowSize <= WindowWidthSizeClass.Compact) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 32.dp, top = 16.dp, end = 32.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (state.image.drawing == null || state.isCorrect != null) {
          BorderCoilImage(url = state.image.url)
        } else {
          AnimationDrawing(
            drawing = state.image.drawing,
            modifier = Modifier
              .fillAppropriateWidth()
              .aspectRatio(1f)
              .border(2.dp, MaterialTheme.colorScheme.outline)
          )
        }
        HintAndAnswerField(
          answers = state.image.answer,
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
          .padding(start = 32.dp, top = 16.dp, end = 32.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.Bottom
      ) {
        BorderCoilImage(url = state.image.url)
        HintAndAnswerField(
          answers = state.image.answer,
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
private fun QuizFriendTitleOrPen(
  isCorrect: Boolean?,
  providerName: String,
  pen: Int,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.End
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        if (isCorrect == null) stringResource(R.string.quiz_friend_title, providerName) else "",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge
      )
      Text(
        if (isCorrect == null) stringResource(R.string.quiz_friend_subtitle) else "",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge
      )
    }
    if (isCorrect != null) {
      PenCountView(penCount = pen)
    }
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
    Text(
      text = when (isCorrect) {
        true -> stringResource(R.string.quiz_user_correct)
        false -> stringResource(R.string.quiz_user_wrong)
        else -> ""
      },
      color = when (isCorrect) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.surface
      },
      style = MaterialTheme.typography.titleLarge
    )
    GTextField(
      value = answer,
      onValueChange = onChangeAnswer,
      modifier = Modifier.fillMaxWidth(),
      enabled = isCorrect == null,
      label = stringResource(R.string.quiz_friend_label)
    )
    GButton(
      text = when (isCorrect == null) {
        true -> stringResource(R.string.quiz_friend_submit_button_hidden)
        false -> stringResource(R.string.quiz_friend_submit_button_not_hidden)
      },
      onClick = onClickSubmitButton,
      modifier = Modifier.width(128.dp),
      enabled = answer.isNotBlank(),
      colors = ButtonColor.Secondary
    )
  }
}

@Composable
fun AnimationDrawing(
  drawing: DrawingUIModel,
  modifier: Modifier = Modifier
) {
  val animatable = remember { Animatable(0f) }
  val pathMeasure = remember { PathMeasure() }
  var currentLineIndex by remember { mutableIntStateOf(0) }

  LaunchedEffect(currentLineIndex) {
    if (currentLineIndex > drawing.drawing.lastIndex) return@LaunchedEffect
    val currentLine = drawing.drawing[currentLineIndex]
    pathMeasure.setPath(currentLine.toPath(), false)

    animatable.run {
      delay(300)
      animateTo(
        1f,
        tween(
          durationMillis = pathMeasure.length.toInt(),
          easing = LinearEasing
        )
      )
      if (value == 1.0f) currentLineIndex++
      snapTo(0f)
    }
  }

  val animatedPath = remember {
    derivedStateOf {
      Path().apply {
        pathMeasure.getSegment(
          0f,
          animatable.value * pathMeasure.length,
          this,
          true
        )
      }
    }
  }

  Canvas(modifier = modifier.clipToBounds()) {
    drawing.drawing.subList(0, currentLineIndex).forEach { drawLineUIModel(it) }
    if (currentLineIndex < drawing.drawing.size) {
      val currentLine = drawing.drawing[currentLineIndex]
      drawPath(
        path = animatedPath.value,
        color = Color(currentLine.color),
        style = Stroke(
          width = currentLine.width,
          cap = StrokeCap.Round
        )
      )
    }
  }
}

@PhonePortraitPreview
@Composable
private fun QuizFriendScreenPhonePortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    QuizFriendScreen(
      QuizFriendState(
        image = ImageUIModel(
          answer = listOf("그림")
        )
      )
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun QuizFriendScreenPhoneLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    QuizFriendScreen(
      QuizFriendState(
        image = ImageUIModel(
          answer = listOf("그림")
        )
      )
    )
  }
}

@TabletPortraitPreview
@Composable
private fun QuizFriendScreenTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    QuizFriendScreen(
      QuizFriendState(
        image = ImageUIModel(
          answer = listOf("그림")
        )
      )
    )
  }
}

@TabletLandscapePreview
@Composable
private fun QuizFriendScreenTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    QuizFriendScreen(
      QuizFriendState(
        image = ImageUIModel(
          answer = listOf("그림")
        )
      )
    )
  }
}