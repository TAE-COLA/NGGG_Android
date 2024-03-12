package com.kakao.presentation.ui.screen.drawing_user_result

import android.graphics.Bitmap
import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class DrawingUserResultContract {

  @Parcelize
  data class DrawingUserResultState(
    val drawing: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
  ) : ViewModelContract.State, Parcelable

  sealed interface DrawingUserResultEvent : ViewModelContract.Event {
    data object OnClickHomeButton : DrawingUserResultEvent
    data object OnClickShareButton : DrawingUserResultEvent
  }

  sealed interface DrawingUserResultReduce : ViewModelContract.Reduce {
    data class UpdateDrawing(val drawing: Bitmap) : DrawingUserResultReduce
  }

  sealed interface DrawingUserResultSideEffect : ViewModelContract.SideEffect {
    data object NavigateToHome : DrawingUserResultSideEffect
    data class ShowSnackbar(val message: UiText) : DrawingUserResultSideEffect
    data class ShareImageQuiz(val image: ImageUIModel): DrawingUserResultSideEffect
  }
}