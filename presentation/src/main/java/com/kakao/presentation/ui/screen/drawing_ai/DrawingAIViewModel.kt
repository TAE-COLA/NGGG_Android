package com.kakao.presentation.ui.screen.drawing_ai

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.QuizConfig.MAX_SELECTABLE_PROMPTS
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetKeywords
import com.kakao.domain.usecase.GetUser
import com.kakao.domain.usecase.UsePen
import com.kakao.presentation.R
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.drawing_ai.DrawingAIContract.DrawingAIEvent
import com.kakao.presentation.ui.screen.drawing_ai.DrawingAIContract.DrawingAIReduce
import com.kakao.presentation.ui.screen.drawing_ai.DrawingAIContract.DrawingAISideEffect
import com.kakao.presentation.ui.screen.drawing_ai.DrawingAIContract.DrawingAIState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingAIViewModel @Inject constructor(
  private val getKeywords: GetKeywords,
  private val getUser: GetUser,
  private val usePen: UsePen,
  savedStateHandle: SavedStateHandle
) : BaseStateViewModel<DrawingAIState, DrawingAIEvent, DrawingAIReduce, DrawingAISideEffect>(
  savedStateHandle
) {
  override fun createInitialState(savedState: Parcelable?): DrawingAIState {
    return savedState as? DrawingAIState ?: DrawingAIState()
  }

  init {
    initialize()
  }

  @SuppressLint("ResourceType")
  override fun handleEvents(event: DrawingAIEvent) {
    when (event) {
      is DrawingAIEvent.OnClickBackButton -> sendSideEffect(DrawingAISideEffect.PopBackStack)
      is DrawingAIEvent.OnChangeSearchKeyword -> updateState(DrawingAIReduce.UpdateSearchKeyword(event.keyword))
      is DrawingAIEvent.OnClickCreateButton -> launch(showLoading = false) {
        usePen(1)
        sendSideEffect(DrawingAISideEffect.NavigateToDrawingAIResult)
        updateState(DrawingAIReduce.UpdateButtonAvailable)
      }

      is DrawingAIEvent.OnClickWordChip -> {
        if (viewState.value.selectedKeywords.size < MAX_SELECTABLE_PROMPTS) {
          updateState(DrawingAIReduce.AddSelectedKeyword(event.keyword))
        } else {
          sendSideEffect(DrawingAISideEffect.ShowSnackBar(UiText.StringResource(R.string.drawing_ai_max_selectable)))
        }
      }

      is DrawingAIEvent.OnClickSelectedChipRemove -> {
        updateState(DrawingAIReduce.RemoveSelectedKeyword(event.index))
      }

    }
  }

  override fun reduceState(state: DrawingAIState, reduce: DrawingAIReduce): DrawingAIState =
    when (reduce) {
      is DrawingAIReduce.AddSearchKeywords -> state.copy(keywordList = state.keywordList + reduce.keywords)
      is DrawingAIReduce.UpdatePenCount -> state.copy(pen = reduce.penCount)
      is DrawingAIReduce.UpdateSearchKeyword -> state.copy(keyword = reduce.keyword)
      is DrawingAIReduce.AddSelectedKeyword -> state.copy(selectedKeywords = state.selectedKeywords + reduce.keyword)
      is DrawingAIReduce.RemoveSelectedKeyword -> state.copy(
        selectedKeywords = state.selectedKeywords.filterIndexed { idx, _ -> idx != reduce.index }
      )
      is DrawingAIReduce.UpdateButtonAvailable -> state.copy(buttonAvailable = false)
    }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(DrawingAISideEffect.ShowSnackBar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  private fun initialize() {
    launch {
      val penCount = getUser().pen

      updateState(DrawingAIReduce.UpdatePenCount(penCount))
    }
    updateState(DrawingAIReduce.AddSearchKeywords(getKeywords().map { it.asUI() }))
  }
}