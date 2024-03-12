package com.kakao.presentation.ui.screen.quiz_friend

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kakao.core.PromptPack.splitWithDelimiters
import com.kakao.core.error.FireStoreError
import com.kakao.core.error.NetworkError
import com.kakao.core.error.UnknownError
import com.kakao.domain.usecase.GetImageById
import com.kakao.domain.usecase.GetUser
import com.kakao.domain.usecase.GivePen
import com.kakao.domain.usecase.InsertUserQuiz
import com.kakao.presentation.base.BaseStateViewModel
import com.kakao.presentation.model.mapper.asDomain
import com.kakao.presentation.model.mapper.asUI
import com.kakao.presentation.ui.screen.quiz_friend.QuizFriendContract.QuizFriendEvent
import com.kakao.presentation.ui.screen.quiz_friend.QuizFriendContract.QuizFriendReduce
import com.kakao.presentation.ui.screen.quiz_friend.QuizFriendContract.QuizFriendSideEffect
import com.kakao.presentation.ui.screen.quiz_friend.QuizFriendContract.QuizFriendState
import com.kakao.presentation.utility.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizFriendViewModel @Inject constructor(
  private val getUser: GetUser,
  private val getImageById: GetImageById,
  private val givePen: GivePen,
  private val insertUserQuiz: InsertUserQuiz,
  private val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<QuizFriendState, QuizFriendEvent, QuizFriendReduce, QuizFriendSideEffect>(savedStateHandle) {
  override fun createInitialState(savedState: Parcelable?): QuizFriendState {
    return savedState as? QuizFriendState ?: QuizFriendState()
  }

  init {
    launch {
      initialize()
    }
  }

  override fun handleEvents(event: QuizFriendEvent) {
    when (event) {
      is QuizFriendEvent.OnClickCloseButton -> sendSideEffect(QuizFriendSideEffect.OpenExitDialog)
      is QuizFriendEvent.OnChangeAnswer -> updateState(QuizFriendReduce.UpdateAnswer(event.answer))
      is QuizFriendEvent.OnClickSubmitButton -> launch {
        if(currentState.isCorrect != null) {
          sendSideEffect(QuizFriendSideEffect.NavigateToHome)
        } else {
          val typedAnswers = currentState.answer.splitWithDelimiters().sorted()
          val answers = currentState.image.answer.sorted()
          val isCorrect = typedAnswers == answers
          updateState(QuizFriendReduce.UpdateIsCorrect(isCorrect))
          if (isCorrect) givePen()
          saveQuizHistory(isCorrect)
        }
      }
    }
  }

  override fun reduceState(state: QuizFriendState, reduce: QuizFriendReduce): QuizFriendState =
    when (reduce) {
      is QuizFriendReduce.UpdateUser -> state.copy(user = reduce.user)
      is QuizFriendReduce.UpdateProviderName -> state.copy(providerName = reduce.providerName)
      is QuizFriendReduce.UpdateImage -> state.copy(image = reduce.image)
      is QuizFriendReduce.UpdateAnswer -> state.copy(answer = reduce.answer)
      is QuizFriendReduce.UpdateIsCorrect -> state.copy(isCorrect = reduce.isCorrect)
    }

  override fun handleErrors(error: Throwable) {
    sendSideEffect(QuizFriendSideEffect.ShowSnackbar(UiText.DynamicString(error.message ?: UnknownError.UNKNOWN)))
    when (error) {
      is FireStoreError.NoImage -> sendSideEffect(QuizFriendSideEffect.NavigateToHome)
      is NetworkError.NetworkNotConnected -> sendSideEffect(QuizFriendSideEffect.NavigateToHome)
    }
  }

  private suspend fun initialize() {
    val user = getUser().asUI()

    updateState(QuizFriendReduce.UpdateUser(user))

    val imageId = checkNotNull(savedStateHandle.get<String?>("imageId"))
    val image = getImageById(imageId).let { it.asUI(it.answer.splitWithDelimiters()) }

    updateState(QuizFriendReduce.UpdateImage(image))


    val provider = getUser(image.userId).asUI()
    updateState(QuizFriendReduce.UpdateProviderName(provider.name))
  }

  private fun givePen() {
    launch {
      givePen(1)

      val user = getUser().asUI()
      updateState(QuizFriendReduce.UpdateUser(user))
    }
  }

  private suspend fun saveQuizHistory(isCorrect: Boolean) {
    val answer = currentState.image.answer.joinToString(", ")

    insertUserQuiz(
      images = listOf(currentState.image.asDomain(answer)),
      myAnswer = listOf(currentState.answer),
      score = listOf(isCorrect)
    )
  }
}
