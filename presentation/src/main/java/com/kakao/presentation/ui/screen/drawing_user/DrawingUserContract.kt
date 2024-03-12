package com.kakao.presentation.ui.screen.drawing_user

import android.os.Parcelable
import com.kakao.presentation.base.ViewModelContract
import com.kakao.presentation.model.KeywordUIModel
import com.kakao.presentation.utility.UiText
import kotlinx.parcelize.Parcelize

class DrawingUserContract {
  @Parcelize
  data class DrawingUserState(
    val keyword: String = "",
    val keywordList: List<KeywordUIModel> = emptyList(),
    val selectedKeywords: List<KeywordUIModel> = emptyList(),
  ) : ViewModelContract.State, Parcelable

  sealed interface DrawingUserEvent : ViewModelContract.Event {
    data class OnChangeSearchKeyword(val keyword: String) : DrawingUserEvent
    data class OnClickWordChip(val keyword: KeywordUIModel) : DrawingUserEvent
    data class OnClickSelectedChipRemove(val index: Int) : DrawingUserEvent
    data object OnClickBackButton : DrawingUserEvent
    data object OnClickNextButton : DrawingUserEvent
  }

  sealed interface DrawingUserReduce : ViewModelContract.Reduce {
    data class AddSearchKeywords(val keywords: List<KeywordUIModel>) : DrawingUserReduce
    data class UpdateSearchKeyword(val keyword: String) : DrawingUserReduce
    data class AddSelectedKeyword(val keyword: KeywordUIModel) : DrawingUserReduce
    data class RemoveSelectedKeyword(val index: Int) : DrawingUserReduce
  }

  sealed interface DrawingUserSideEffect : ViewModelContract.SideEffect {
    data object PopBackStack : DrawingUserSideEffect
    data object NavigateToDrawingUserSketch : DrawingUserSideEffect
    data class ShowSnackbar(val message: UiText) : DrawingUserSideEffect
  }
}