package com.kakao.presentation.ui.screen.drawing_user_sketch

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.DrawingConfig
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.SetImageCache
import com.kakao.domain.usecase.SetStringCache
import com.kakao.presentation.R
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.KeywordUIModelArgType
import com.kakao.presentation.model.SelectableWidthUIModel
import com.kakao.presentation.ui.screen.drawing_user_sketch.DrawingUserSketchContract.DrawingUserSketchEvent
import com.kakao.presentation.ui.screen.drawing_user_sketch.DrawingUserSketchContract.DrawingUserSketchReduce
import com.kakao.presentation.ui.screen.drawing_user_sketch.DrawingUserSketchContract.DrawingUserSketchSideEffect
import com.kakao.presentation.ui.screen.drawing_user_sketch.DrawingUserSketchContract.DrawingUserSketchState
import com.kakao.presentation.utility.UiText
import com.kakao.presentation.utility.toByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingUserSketchViewModel @Inject constructor(
  private val setStringCache: SetStringCache,
  private val setImageCache: SetImageCache,
  private val savedStateHandle: SavedStateHandle
) :
  BaseStateViewModel<DrawingUserSketchState, DrawingUserSketchEvent, DrawingUserSketchReduce, DrawingUserSketchSideEffect>(
    savedStateHandle
  ) {
  override fun createInitialState(savedState: Parcelable?): DrawingUserSketchState {
    return savedState as? DrawingUserSketchState ?: DrawingUserSketchState()
  }

  init {
    initialize()
  }

  @SuppressLint("ResourceType")
  override fun handleEvents(event: DrawingUserSketchEvent) {
    when (event) {
      is DrawingUserSketchEvent.OnClickBackButton -> sendSideEffect(DrawingUserSketchSideEffect.PopBackStack)
      is DrawingUserSketchEvent.OnDrawLine -> {
        updateState(DrawingUserSketchReduce.UpdateDrawingLine(currentState.drawing.drawing + event.line))
        updateState(DrawingUserSketchReduce.UpdateRedoStack(emptyList()))
      }
      is DrawingUserSketchEvent.OnClickSelectableColor -> updateState(
        DrawingUserSketchReduce.UpdateSelectedColor(
          event.color
        )
      )

      is DrawingUserSketchEvent.OnClickSelectableWidth -> updateState(
        DrawingUserSketchReduce.UpdateSelectedWidth(
          event.width
        )
      )

      is DrawingUserSketchEvent.OnClickFinishButton -> launch {
        event.bitmap?.let { cacheImage(it) } ?: throw Error("Fail cache image")
      }

      is DrawingUserSketchEvent.OnClickUndoButton -> {
        val lastDrawingLine = currentState.drawing.drawing.last()
        updateState(DrawingUserSketchReduce.UpdateDrawingLine(currentState.drawing.drawing.subList(0, currentState.drawing.drawing.lastIndex)))
        updateState(DrawingUserSketchReduce.UpdateRedoStack(currentState.redoStack + lastDrawingLine))
      }

      is DrawingUserSketchEvent.OnClickRedoButton -> {
        val lastRedoLine = currentState.redoStack.last()
        updateState(DrawingUserSketchReduce.UpdateRedoStack(currentState.redoStack.subList(0, currentState.redoStack.lastIndex)))
        updateState(DrawingUserSketchReduce.UpdateDrawingLine(currentState.drawing.drawing + lastRedoLine))
      }
    }
  }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(DrawingUserSketchSideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  override fun reduceState(state: DrawingUserSketchState, reduce: DrawingUserSketchReduce): DrawingUserSketchState =
    when (reduce) {
      is DrawingUserSketchReduce.UpdateSelectedKeyword -> state.copy(selectedKeyword = reduce.keyword)
      is DrawingUserSketchReduce.UpdateDrawingLine -> state.copy(drawing = state.drawing.copy(drawing = reduce.lines))
      is DrawingUserSketchReduce.UpdateRedoStack -> state.copy(redoStack = reduce.redo)
      is DrawingUserSketchReduce.UpdateSelectedColor -> state.copy(brush = state.brush.copy(color = reduce.color))
      is DrawingUserSketchReduce.UpdateSelectedWidth -> state.copy(brush = state.brush.copy(width = reduce.width))
      is DrawingUserSketchReduce.UpdateSelectableColor -> state.copy(selectableColor = reduce.colorList)
      is DrawingUserSketchReduce.UpdateSelectableWidth -> state.copy(selectableWidth = reduce.widthList)
    }

  private fun initialize() {
    val keywordsJson: String = checkNotNull(savedStateHandle["keyword"])
    val keywords = KeywordUIModelArgType().fromJsonParse(keywordsJson)

    val widthDrawableIds = listOf(
      R.drawable.ic_thickness_1,
      R.drawable.ic_thickness_2,
      R.drawable.ic_thickness_3,
      R.drawable.ic_thickness_4,
      R.drawable.ic_thickness_5
    )

    val widths = widthDrawableIds.zip(DrawingConfig.widths) { drawable, width ->
      SelectableWidthUIModel(drawable, width)
    }

    updateState(DrawingUserSketchReduce.UpdateSelectedKeyword(keywords))
    updateState(DrawingUserSketchReduce.UpdateSelectableColor(DrawingConfig.colors))
    updateState(DrawingUserSketchReduce.UpdateSelectableWidth(widths))
  }

  private suspend fun cacheImage(bitmap: Bitmap) {
    val screenshot = bitmap.toByteArray()
    val drawingString = currentState.drawing.toString()

    setImageCache(screenshot)
    setStringCache(drawingString)
    sendSideEffect(DrawingUserSketchSideEffect.NavigateToDrawingUserResult)
  }
}