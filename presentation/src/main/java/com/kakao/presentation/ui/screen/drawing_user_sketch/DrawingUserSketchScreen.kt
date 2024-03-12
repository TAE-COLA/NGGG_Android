package com.kakao.presentation.ui.screen.drawing_user_sketch

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.presentation.R
import com.kakao.presentation.model.BrushUIModel
import com.kakao.presentation.model.DrawingUIModel
import com.kakao.presentation.model.KeywordUIModel
import com.kakao.presentation.model.LinePoint
import com.kakao.presentation.model.LineUIModel
import com.kakao.presentation.model.SelectableWidthUIModel
import com.kakao.presentation.ui.screen.drawing_user_sketch.DrawingUserSketchContract.*
import com.kakao.presentation.ui.view.ButtonColor
import com.kakao.presentation.ui.view.ChipBox
import com.kakao.presentation.ui.view.GButton
import com.kakao.presentation.ui.view.LoadingDialog
import com.kakao.presentation.ui.view.ResolutionErrorScreen
import com.kakao.presentation.ui.view.Screen
import com.kakao.presentation.ui.view.ScreenshotBox
import com.kakao.presentation.ui.view.ScreenshotController
import com.kakao.presentation.ui.view.rememberScreenshotController
import com.kakao.presentation.utility.LocalWindowSize
import com.kakao.presentation.utility.PhoneLandscapePreview
import com.kakao.presentation.utility.PhonePortraitPreview
import com.kakao.presentation.utility.ScreenPreview
import com.kakao.presentation.utility.TabletLandscapePreview
import com.kakao.presentation.utility.TabletPortraitPreview
import com.kakao.presentation.utility.WindowSize
import com.kakao.presentation.utility.drawLineUIModel
import com.kakao.presentation.utility.fillAppropriateWidth
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DrawingUserSketchRoute(
  navigateToDrawingUserResult: (List<KeywordUIModel>) -> Unit,
  popBackStack: () -> Unit,
  showSnackbar: suspend (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: DrawingUserSketchViewModel = hiltViewModel(),
  context: Context = LocalContext.current
) {
  val drawingUserSketchState by viewModel.viewState.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)
  val screenshotController = rememberScreenshotController()

  LaunchedEffect(viewModel.sideEffect) {
    viewModel.sideEffect.collectLatest {
      when (it) {
        is DrawingUserSketchSideEffect.NavigateToDrawingUserResult -> navigateToDrawingUserResult(
          drawingUserSketchState.selectedKeyword
        )

        is DrawingUserSketchSideEffect.PopBackStack -> popBackStack()
        is DrawingUserSketchSideEffect.ShowSnackbar -> showSnackbar(it.message.asString(context))
      }
    }
  }

  if (isLoading) LoadingDialog()
  else DrawingUserSketchScreen(
    state = drawingUserSketchState,
    screenshotController = screenshotController,
    modifier = modifier,
    onClickBackButton = { viewModel.sendEvent(DrawingUserSketchEvent.OnClickBackButton) },
    onDrawingLine = { viewModel.sendEvent(DrawingUserSketchEvent.OnDrawLine(it)) },
    onClickSelectableColor = { viewModel.sendEvent(DrawingUserSketchEvent.OnClickSelectableColor(it)) },
    onClickSelectableWidth = { viewModel.sendEvent(DrawingUserSketchEvent.OnClickSelectableWidth(it)) },
    onClickFinishButton = {
      viewModel.sendEvent(
        DrawingUserSketchEvent.OnClickFinishButton(
          screenshotController.capture()
        )
      )
    },
    onClickUndoButton = { viewModel.sendEvent(DrawingUserSketchEvent.OnClickUndoButton) },
    onClickRedoButton = { viewModel.sendEvent(DrawingUserSketchEvent.OnClickRedoButton) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawingUserSketchScreen(
  state: DrawingUserSketchState,
  screenshotController: ScreenshotController,
  modifier: Modifier = Modifier,
  onClickBackButton: () -> Unit = {},
  onDrawingLine: (LineUIModel) -> Unit = {},
  onClickSelectableColor: (Long) -> Unit = {},
  onClickSelectableWidth: (Float) -> Unit = {},
  onClickFinishButton: () -> Unit = {},
  onClickUndoButton: () -> Unit = {},
  onClickRedoButton: () -> Unit = {},
  windowSize: WindowSize = LocalWindowSize.current
) {
  Screen(
    modifier = modifier,
    scrollable = false,
    topAppBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.drawing_user_sketch_title)) },
        navigationIcon = {
          IconButton(onClick = onClickBackButton) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) {
    if (windowSize <= WindowHeightSizeClass.Compact) {
      ResolutionErrorScreen(onClickBackButton)
    } else if (windowSize >= WindowWidthSizeClass.Medium) {
      Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
          modifier = Modifier.fillAppropriateWidth(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          TitleText()
          SelectedWords(
            selectedKeywords = state.selectedKeyword,
            modifier = Modifier.padding(top = 16.dp)
          )
          DrawingCanvas(
            drawing = state.drawing,
            brush = state.brush,
            screenshotController = screenshotController,
            modifier = Modifier
              .fillAppropriateWidth()
              .padding(top = 33.dp),
            onDrawingLine = onDrawingLine
          )
          CompleteButton(
            modifier = Modifier.padding(top = 16.dp),
            onClickFinishButton = onClickFinishButton
          )
        }
        Column(
          modifier = Modifier.padding(top = 32.dp, start = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            VerticalPalette(
              colors = state.selectableColor,
              selectedColor = state.brush.color,
              onSelectColor = onClickSelectableColor
            )
            VerticalSelectableWidth(
              selectableWidth = state.selectableWidth,
              selectedWidth = state.brush.width,
              onClickSelectableWidth = onClickSelectableWidth
            )
          }
          CommandButton(
            lines = state.drawing.drawing,
            redoStack = state.redoStack,
            onClickUndoButton = onClickUndoButton,
            onClickRedoButton = onClickRedoButton
          )
        }
      }
    } else {
      Column(
        Modifier
          .fillMaxSize()
          .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        TitleText(modifier.padding(top = 32.dp))
        SelectedWords(
          selectedKeywords = state.selectedKeyword,
          modifier = Modifier.padding(top = 16.dp)
        )
        DrawingCanvas(
          drawing = state.drawing,
          brush = state.brush,
          screenshotController = screenshotController,
          modifier = Modifier
            .fillAppropriateWidth()
            .padding(top = 33.dp),
          onDrawingLine = onDrawingLine
        )
        Row(
          Modifier
            .fillAppropriateWidth()
            .padding(top = 16.dp)
        ) {
          SelectableWidth(
            selectableWidth = state.selectableWidth,
            selectedWidth = state.brush.width,
            onClickSelectableWidth = onClickSelectableWidth
          )
          Spacer(modifier = Modifier.weight(1f))
          CommandButton(
            lines = state.drawing.drawing,
            redoStack = state.redoStack,
            onClickUndoButton = onClickUndoButton,
            onClickRedoButton = onClickRedoButton
          )
        }
        Palette(
          modifier = Modifier
            .fillAppropriateWidth()
            .padding(top = 16.dp),
          colors = state.selectableColor,
          selectedColor = state.brush.color,
          onSelectColor = onClickSelectableColor
        )
        Spacer(modifier = Modifier.weight(1f))
        CompleteButton(
          modifier = Modifier.padding(bottom = 32.dp),
          onClickFinishButton = onClickFinishButton
        )
      }
    }
  }
}

@Composable
private fun TitleText(modifier: Modifier = Modifier) {
  Text(
    stringResource(R.string.drawing_user_sketch_sub_text),
    style = MaterialTheme.typography.titleMedium,
    modifier = modifier
  )
}

@Composable
private fun SelectedWords(
  selectedKeywords: List<KeywordUIModel>,
  modifier: Modifier = Modifier
) {
  ChipBox(
    label = stringResource(R.string.drawing_user_sketch_chip_box_label),
    chips = selectedKeywords,
    chipRemoveAvailable = false,
    modifier = modifier
      .fillAppropriateWidth()
  )
}

@Composable
private fun SelectableWidth(
  selectableWidth: List<SelectableWidthUIModel>,
  selectedWidth: Float,
  onClickSelectableWidth: (Float) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    selectableWidth.forEach {
      Image(
        painterResource(id = it.drawableId),
        contentDescription = null,
        modifier = Modifier
          .alpha(if (it.width == selectedWidth) 0.3f else 1f)
          .clickable {
            onClickSelectableWidth(it.width)
          }
      )
    }
  }
}

@Composable
private fun VerticalSelectableWidth(
  selectableWidth: List<SelectableWidthUIModel>,
  selectedWidth: Float,
  onClickSelectableWidth: (Float) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    selectableWidth.forEach {
      Image(
        painterResource(id = it.drawableId),
        contentDescription = null,
        modifier = Modifier
          .alpha(if (it.width == selectedWidth) 0.3f else 1f)
          .clickable {
            onClickSelectableWidth(it.width)
          }
      )
    }
  }
}

@Composable
private fun CommandButton(
  lines: List<LineUIModel>,
  redoStack: List<LineUIModel>,
  onClickUndoButton: () -> Unit,
  onClickRedoButton: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Image(
      painterResource(id = R.drawable.ic_undo),
      contentDescription = null,
      modifier = modifier
        .alpha(if (lines.isEmpty()) 0.3f else 1f)
        .clickable(
          enabled = lines.isNotEmpty(),
          onClick = onClickUndoButton
        )
    )
    Image(
      painterResource(id = R.drawable.ic_redo),
      contentDescription = null,
      modifier = modifier
        .alpha(if (redoStack.isEmpty()) 0.3f else 1f)
        .clickable(
          enabled = redoStack.isNotEmpty(),
          onClick = onClickRedoButton
        )
    )
  }
}

@Composable
private fun Palette(
  colors: List<Long>,
  selectedColor: Long,
  onSelectColor: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    colors.map { colorValue ->
      ColorBox(
        colorValue = colorValue,
        isSelected = colorValue == selectedColor,
        onSelectColor = onSelectColor
      )
    }
  }
}

@Composable
private fun VerticalPalette(
  colors: List<Long>,
  selectedColor: Long,
  onSelectColor: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    colors.map { colorValue ->
      ColorBox(
        colorValue = colorValue,
        isSelected = colorValue == selectedColor,
        onSelectColor = onSelectColor
      )
    }
  }
}

@Composable
private fun ColorBox(
  colorValue: Long,
  isSelected: Boolean,
  onSelectColor: (Long) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .width(24.dp)
      .aspectRatio(1f)
      .background(
        if (isSelected) Color(colorValue).copy(alpha = 0.3f)
        else Color(colorValue)
      )
      .border(
        width = 2.dp,
        color = MaterialTheme.colorScheme.outline,
        shape = RoundedCornerShape(2.dp)
      )
      .clickable { onSelectColor(colorValue) }
  )
}

@Composable
private fun CompleteButton(
  onClickFinishButton: () -> Unit,
  modifier: Modifier = Modifier
) {
  GButton(
    text = stringResource(R.string.drawing_user_sketch_button_text),
    colors = ButtonColor.Secondary,
    modifier = modifier,
    onClick = onClickFinishButton
  )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DrawingCanvas(
  drawing: DrawingUIModel,
  brush: BrushUIModel,
  screenshotController: ScreenshotController,
  onDrawingLine: (LineUIModel) -> Unit,
  modifier: Modifier = Modifier,
) {
  var currentDrawingLine by remember { mutableStateOf(LineUIModel()) }

  Box(modifier) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.TopCenter)
        .padding(horizontal = 10.dp),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      repeat(8) {
        Image(
          painterResource(id = R.drawable.img_spring),
          contentDescription = null,
          modifier = Modifier.size(32.dp, 32.dp)
        )
      }
    }
    Box(
      modifier = Modifier
        .padding(top = 11.dp)
        .border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.outline,
        )
    ) {
      ScreenshotBox(
        modifier = Modifier.padding(top = 21.dp),
        screenshotController = screenshotController
      ) {
        key(brush) {
          Canvas(
            modifier = Modifier
              .aspectRatio(1f)
              .clipToBounds()
              .pointerInput(Unit) {
                detectTapGestures(
                  onPress = {
                    currentDrawingLine = LineUIModel(
                      linePath = listOf(it.asLinePoint()),
                      color = brush.color,
                      width = brush.width
                    )
                  },
                  onTap = {
                    onDrawingLine(currentDrawingLine)
                    currentDrawingLine = LineUIModel()
                  }
                )
              }
              .pointerInput(Unit) {
                detectDragGestures(
                  onDrag = { change, _ ->
                    val pointsFromHistory = change.historical
                      .map { it.position.asLinePoint() }

                    val newPoint = pointsFromHistory + change.position.asLinePoint()
                    currentDrawingLine = currentDrawingLine.copy(
                      linePath = currentDrawingLine.linePath + newPoint,
                    )
                  },
                  onDragEnd = {
                    onDrawingLine(currentDrawingLine)
                    currentDrawingLine = LineUIModel()
                  }
                )
              }
          ) {
            (drawing.drawing + currentDrawingLine).forEach { line ->
              drawLineUIModel(line)
            }
          }
        }
      }
    }
  }
}

private fun Offset.asLinePoint(): LinePoint =
  LinePoint(x = x, y = y)

@PhonePortraitPreview
@Composable
private fun DrawingUserSketchPortraitPreview() {
  ScreenPreview(ScreenPreview.PHONE_PORTRAIT) {
    DrawingUserSketchScreen(
      DrawingUserSketchState(),
      rememberScreenshotController()
    )
  }
}

@PhoneLandscapePreview
@Composable
private fun DrawingUserSketchLandscapePreview() {
  ScreenPreview(ScreenPreview.PHONE_LANDSCAPE) {
    DrawingUserSketchScreen(
      DrawingUserSketchState(),
      rememberScreenshotController()
    )
  }
}

@TabletPortraitPreview
@Composable
private fun DrawingUserSketchTabletPortraitPreview() {
  ScreenPreview(ScreenPreview.TABLET_PORTRAIT) {
    DrawingUserSketchScreen(
      DrawingUserSketchState(),
      rememberScreenshotController()
    )
  }
}

@TabletLandscapePreview
@Composable
private fun DrawingUserSketchTabletLandscapePreview() {
  ScreenPreview(ScreenPreview.TABLET_LANDSCAPE) {
    DrawingUserSketchScreen(
      DrawingUserSketchState(),
      rememberScreenshotController()
    )
  }
}