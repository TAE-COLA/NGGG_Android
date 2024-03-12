package com.kakao.presentation.ui.screen.drawing_user

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.QuizConfig.MAX_SELECTABLE_PROMPTS
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetKeywords
import com.kakao.presentation.R
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.drawing_user.DrawingUserContract.DrawingUserEvent
import com.kakao.presentation.ui.screen.drawing_user.DrawingUserContract.DrawingUserReduce
import com.kakao.presentation.ui.screen.drawing_user.DrawingUserContract.DrawingUserSideEffect
import com.kakao.presentation.ui.screen.drawing_user.DrawingUserContract.DrawingUserState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingUserViewModel @Inject constructor(
  private val getKeywords: GetKeywords,
  savedStateHandle: SavedStateHandle
) :
  BaseStateViewModel<DrawingUserState, DrawingUserEvent, DrawingUserReduce, DrawingUserSideEffect>(
    savedStateHandle
  ) {
  override fun createInitialState(savedState: Parcelable?): DrawingUserState {
    return savedState as? DrawingUserState ?: DrawingUserState()
  }

  init {
    initialize()
  }

  @SuppressLint("ResourceType")
  override fun handleEvents(event: DrawingUserEvent) {
    when (event) {
      is DrawingUserEvent.OnClickBackButton -> sendSideEffect(DrawingUserSideEffect.PopBackStack)
      is DrawingUserEvent.OnClickNextButton -> sendSideEffect(DrawingUserSideEffect.NavigateToDrawingUserSketch)
      is DrawingUserEvent.OnChangeSearchKeyword -> updateState(DrawingUserReduce.UpdateSearchKeyword(event.keyword))
      is DrawingUserEvent.OnClickSelectedChipRemove -> updateState(DrawingUserReduce.RemoveSelectedKeyword(event.index))
      is DrawingUserEvent.OnClickWordChip -> {
        if (viewState.value.selectedKeywords.size < MAX_SELECTABLE_PROMPTS) {
          updateState(DrawingUserReduce.AddSelectedKeyword(event.keyword))
        } else {
          sendSideEffect(DrawingUserSideEffect.ShowSnackbar(UiText.StringResource(R.string.drawing_user_max_selectable)))
        }
      }
    }
  }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(DrawingUserSideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
  }

  override fun reduceState(
    state: DrawingUserState,
    reduce: DrawingUserReduce
  ): DrawingUserState =
    when (reduce) {
      is DrawingUserReduce.AddSelectedKeyword -> state.copy(selectedKeywords = state.selectedKeywords + reduce.keyword)
      is DrawingUserReduce.UpdateSearchKeyword -> state.copy(keyword = reduce.keyword)
      is DrawingUserReduce.AddSearchKeywords -> state.copy(keywordList = state.keywordList + reduce.keywords)
      is DrawingUserReduce.RemoveSelectedKeyword -> state.copy(
        selectedKeywords = state.selectedKeywords.filterIndexed { idx, _ -> idx != reduce.index }
      )
    }

  private fun initialize() {
    updateState(DrawingUserReduce.AddSearchKeywords(getKeywords().map { it.asUI() }))
  }
}