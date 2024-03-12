package com.kakao.presentation.ui.screen.drawing_user_sketch

import android.graphics.Bitmap
import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.BrushUIModel
import com.kakao.presentation.model.DrawingUIModel
import com.kakao.presentation.model.KeywordUIModel
import com.kakao.presentation.model.LineUIModel
import com.kakao.presentation.model.SelectableWidthUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class DrawingUserSketchContract {
  @Parcelize
  data class DrawingUserSketchState(
    val selectedKeyword: List<KeywordUIModel> = emptyList(),
    val drawing: DrawingUIModel = DrawingUIModel(),
    val redoStack: List<LineUIModel> = emptyList(),
    val brush: BrushUIModel = BrushUIModel(),
    val selectableColor: List<Long> = emptyList(),
    val selectableWidth: List<SelectableWidthUIModel> = emptyList()
  ) : ViewModelContract.State, Parcelable

  sealed interface DrawingUserSketchEvent : ViewModelContract.Event {
    data object OnClickBackButton : DrawingUserSketchEvent
    data class OnDrawLine(val line: LineUIModel) : DrawingUserSketchEvent
    data class OnClickSelectableColor(val color: Long) : DrawingUserSketchEvent
    data class OnClickSelectableWidth(val width: Float) : DrawingUserSketchEvent
    data class OnClickFinishButton(val bitmap: Bitmap?) : DrawingUserSketchEvent
    data object OnClickUndoButton : DrawingUserSketchEvent
    data object OnClickRedoButton : DrawingUserSketchEvent
  }

  sealed interface DrawingUserSketchReduce : ViewModelContract.Reduce {
    data class UpdateSelectedKeyword(val keyword: List<KeywordUIModel>) : DrawingUserSketchReduce
    data class UpdateDrawingLine(val lines: List<LineUIModel>) : DrawingUserSketchReduce
    data class UpdateRedoStack(val redo: List<LineUIModel>) : DrawingUserSketchReduce
    data class UpdateSelectedColor(val color: Long) : DrawingUserSketchReduce
    data class UpdateSelectedWidth(val width: Float) : DrawingUserSketchReduce
    data class UpdateSelectableColor(val colorList: List<Long>) : DrawingUserSketchReduce
    data class UpdateSelectableWidth(val widthList: List<SelectableWidthUIModel>) : DrawingUserSketchReduce
  }

  sealed interface DrawingUserSketchSideEffect : ViewModelContract.SideEffect {
    data object NavigateToDrawingUserResult : DrawingUserSketchSideEffect
    data object PopBackStack : DrawingUserSketchSideEffect
    data class ShowSnackbar(val message: UiText) : DrawingUserSketchSideEffect
  }
}