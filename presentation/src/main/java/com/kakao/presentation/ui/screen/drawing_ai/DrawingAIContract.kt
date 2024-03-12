package com.kakao.presentation.ui.screen.drawing_ai

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.KeywordUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class DrawingAIContract {
  @Parcelize
  data class DrawingAIState(
    val keyword: String = "",
    val pen: Int = 0,
    val keywordList: List<KeywordUIModel> = emptyList(),
    val selectedKeywords: List<KeywordUIModel> = emptyList(),
    val buttonAvailable: Boolean = true,
  ) : ViewModelContract.State, Parcelable

  sealed interface DrawingAIEvent : ViewModelContract.Event {
    data class OnChangeSearchKeyword(val keyword: String) : DrawingAIEvent
    data class OnClickWordChip(val keyword: KeywordUIModel) : DrawingAIEvent
    data object OnClickCreateButton : DrawingAIEvent
    data class OnClickSelectedChipRemove(val index: Int) : DrawingAIEvent
    data object OnClickBackButton : DrawingAIEvent
  }

  sealed interface DrawingAIReduce : ViewModelContract.Reduce {
    data class AddSearchKeywords(val keywords: List<KeywordUIModel>) : DrawingAIReduce
    data class UpdatePenCount(val penCount: Int) : DrawingAIReduce
    data class UpdateSearchKeyword(val keyword: String) : DrawingAIReduce
    data class AddSelectedKeyword(val keyword: KeywordUIModel) : DrawingAIReduce
    data class RemoveSelectedKeyword(val index: Int) : DrawingAIReduce
    data object UpdateButtonAvailable : DrawingAIReduce
  }

  sealed interface DrawingAISideEffect : ViewModelContract.SideEffect {
    data object PopBackStack : DrawingAISideEffect
    data object NavigateToDrawingAIResult : DrawingAISideEffect
    data class ShowSnackBar(val message: UiText) : DrawingAISideEffect
  }
}