package com.kakao.presentation.ui.screen.quiz_history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizHistoryViewModel @Inject constructor(
  val savedStateHandle: SavedStateHandle
) : ViewModel()