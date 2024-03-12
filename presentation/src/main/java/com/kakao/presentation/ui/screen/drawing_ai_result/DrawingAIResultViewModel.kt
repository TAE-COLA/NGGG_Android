package com.kakao.presentation.ui.screen.drawing_ai_result

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.error.NetworkError
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetGeneratedImage
import com.kakao.domain.usecase.UploadKarloImage
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.KeywordUIModelArgType
import com.kakao.presentation.model.PromptUIModel
import com.kakao.presentation.model.mapper.asDomain
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.drawing_ai_result.DrawingAIResultContract.DrawingAIResultEvent
import com.kakao.presentation.ui.screen.drawing_ai_result.DrawingAIResultContract.DrawingAIResultReduce
import com.kakao.presentation.ui.screen.drawing_ai_result.DrawingAIResultContract.DrawingAIResultSideEffect
import com.kakao.presentation.ui.screen.drawing_ai_result.DrawingAIResultContract.DrawingAIResultState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DrawingAIResultViewModel @Inject constructor(
  private val getGeneratedImage: GetGeneratedImage,
  private val uploadKarloImage: UploadKarloImage,
  private val savedStateHandle: SavedStateHandle
) :
  BaseStateViewModel<DrawingAIResultState, DrawingAIResultEvent, DrawingAIResultReduce, DrawingAIResultSideEffect>(
    savedStateHandle
  ) {
  override fun createInitialState(savedState: Parcelable?): DrawingAIResultState {
    return savedState as? DrawingAIResultState ?: DrawingAIResultState()
  }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(DrawingAIResultSideEffect.ShowSnackBar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
    when (error) {
      is NetworkError.NetworkNotConnected -> sendSideEffect(DrawingAIResultSideEffect.NavigateToHome)
    }
  }

  init {
    launch {
      initialize()
    }
  }

  override fun handleEvents(event: DrawingAIResultEvent) {
    when (event) {
      is DrawingAIResultEvent.OnClickHomeButton -> sendSideEffect(DrawingAIResultSideEffect.NavigateToHome)
      is DrawingAIResultEvent.OnClickShareButton -> launch { uploadAndShareImage() }
    }
  }

  override fun reduceState(
    state: DrawingAIResultState,
    reduce: DrawingAIResultReduce
  ): DrawingAIResultState =
    when (reduce) {
      is DrawingAIResultReduce.UpdateGeneratedImage -> state.copy(image = reduce.image)
    }

  private suspend fun initialize() {
    val keywordsJson: String = checkNotNull(savedStateHandle["keyword"])
    val keywords = KeywordUIModelArgType().fromJsonParse(keywordsJson)
    val prompt = PromptUIModel(keywords.joinToString(" and ") { it.english })
    val generatedImage = getGeneratedImage(prompt.asDomain()).asUI(keywords)

    updateState(DrawingAIResultReduce.UpdateGeneratedImage(generatedImage))
  }

  private suspend fun uploadAndShareImage() {
    val answerString = currentState.image.answer.joinToString(", ") { it.korean }
    val date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
    val answerList = currentState.image.answer.map { it.korean }

    val image = uploadKarloImage(currentState.image.asDomain(answerString), date).asUI(answerList)
    sendSideEffect(DrawingAIResultSideEffect.ShareImageQuiz(image))
  }
}