package com.kakao.presentation.ui.screen.drawing_user_result

import android.graphics.BitmapFactory
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetImageCache
import com.kakao.domain.usecase.UploadCachedImage
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.KeywordUIModelArgType
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.drawing_user_result.DrawingUserResultContract.DrawingUserResultEvent
import com.kakao.presentation.ui.screen.drawing_user_result.DrawingUserResultContract.DrawingUserResultReduce
import com.kakao.presentation.ui.screen.drawing_user_result.DrawingUserResultContract.DrawingUserResultSideEffect
import com.kakao.presentation.ui.screen.drawing_user_result.DrawingUserResultContract.DrawingUserResultState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DrawingUserResultViewModel @Inject constructor(
  private val getImageCache: GetImageCache,
  private val uploadCachedImage: UploadCachedImage,
  private val savedStateHandle: SavedStateHandle
) :
  BaseStateViewModel<DrawingUserResultState, DrawingUserResultEvent, DrawingUserResultReduce, DrawingUserResultSideEffect>(
    savedStateHandle
  ) {
  override fun createInitialState(savedState: Parcelable?): DrawingUserResultState {
    return savedState as? DrawingUserResultState ?: DrawingUserResultState()
  }

  init {
    initialize()
  }

  override fun handleEvents(event: DrawingUserResultEvent) {
    when (event) {
      is DrawingUserResultEvent.OnClickHomeButton -> sendSideEffect(DrawingUserResultSideEffect.NavigateToHome)
      is DrawingUserResultEvent.OnClickShareButton -> uploadAndShareImage()
    }
  }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(DrawingUserResultSideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  override fun reduceState(
    state: DrawingUserResultState,
    reduce: DrawingUserResultReduce
  ): DrawingUserResultState =
    when (reduce) {
      is DrawingUserResultReduce.UpdateDrawing -> state.copy(drawing = reduce.drawing)
    }

  private fun initialize() = launch {
    val drawingByteArray = getImageCache()
    val drawing = BitmapFactory.decodeByteArray(drawingByteArray, 0, drawingByteArray.size)
    updateState(DrawingUserResultReduce.UpdateDrawing(drawing))
  }

  private fun uploadAndShareImage() = launch {
    val keywordsJson: String = checkNotNull(savedStateHandle["keyword"])
    val keywords = KeywordUIModelArgType().fromJsonParse(keywordsJson)

    val answerList = keywords.map { it.korean }
    val answer = keywords.joinToString(", ") { it.korean }
    val date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
    val image = uploadCachedImage(answer, date).asUI(answerList)

    sendSideEffect(DrawingUserResultSideEffect.ShareImageQuiz(image))
  }
}