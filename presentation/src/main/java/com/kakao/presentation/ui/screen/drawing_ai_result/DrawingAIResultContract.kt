package com.kakao.presentation.ui.screen.drawing_ai_result

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.GeneratedImageUIModel
import com.kakao.presentation.model.ImageUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class DrawingAIResultContract {
  @Parcelize
  data class DrawingAIResultState(
    val image: GeneratedImageUIModel = GeneratedImageUIModel(),
  ) : ViewModelContract.State, Parcelable

  sealed interface DrawingAIResultEvent : ViewModelContract.Event {
    data object OnClickHomeButton : DrawingAIResultEvent
    data object OnClickShareButton : DrawingAIResultEvent
  }

  sealed interface DrawingAIResultReduce : ViewModelContract.Reduce {
    data class UpdateGeneratedImage(val image: GeneratedImageUIModel) : DrawingAIResultReduce
  }

  sealed interface DrawingAIResultSideEffect : ViewModelContract.SideEffect {
    data object NavigateToHome : DrawingAIResultSideEffect
    data class ShowSnackBar(val message: UiText) : DrawingAIResultSideEffect
    data class ShareImageQuiz(val image: ImageUIModel): DrawingAIResultSideEffect
  }
}