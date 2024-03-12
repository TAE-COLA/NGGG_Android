package com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.presentation.R
import com.kakao.presentation.base.BaseFragment
import com.kakao.presentation.databinding.FragmentQuizHistoryDetailBinding
import com.kakao.presentation.ui.screen.quiz_history.QuizHistoryViewModel
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail.QuizHistoryDetailContract.QuizHistoryDetailEvent
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_detail.QuizHistoryDetailContract.QuizHistoryDetailSideEffect
import com.kakao.presentation.utility.ConfigurationForDisplay
import com.kakao.presentation.utility.floatDp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizHistoryDetailFragment :
  BaseFragment<FragmentQuizHistoryDetailBinding>(R.layout.fragment_quiz_history_detail) {

  private val viewModel: QuizHistoryDetailViewModel by viewModels()
  private val activityViewModel: QuizHistoryViewModel by activityViewModels()

  override fun onAttach(context: Context) {
    super.onAttach(context)
    setOnBackButtonPress()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    collectState()
    collectLoading()
    collectSideEffect()
    setEvents()
    initViewPage()
    configureForDisplay()
  }

  private fun collectState() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.viewState.collect {
          binding.state = it
        }
      }
    }
  }

  private fun collectLoading() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.isLoading.collect {
          binding.isLoading = it
        }
      }
    }
  }

  private fun collectSideEffect() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.sideEffect.collectLatest {
          when (it) {
            is QuizHistoryDetailSideEffect.PopBackStack -> {
              findNavController().popBackStack()
            }

            is QuizHistoryDetailSideEffect.ShowSnackbar -> {
              Snackbar.make(
                requireView(),
                it.message.asString(requireContext()),
                Snackbar.LENGTH_SHORT
              ).show()
            }
          }
        }
      }
    }
  }

  private fun setEvents() {
    binding.apply {
      binding.toolbar.setNavigationOnClickListener {
        val key = viewModel.viewState.value.quizId
        activityViewModel.savedStateHandle[key] = binding.vpQuestionCard.currentItem
        viewModel.sendEvent(QuizHistoryDetailEvent.OnClickBackButton)
      }
      onDataUpdated = {
        loadPrevPageState()
      }
    }
  }

  private fun initViewPage() {
    val configuration = activity?.resources?.configuration ?: return
    val orientation = configuration.orientation
    val width = configuration.screenWidthDp
    val height = configuration.screenHeightDp

    var itemOffset = 0
    ConfigurationForDisplay(context)
      .widthLessThan(480, Configuration.ORIENTATION_PORTRAIT) {
        itemOffset = 50.floatDp.toInt()
      }
      .heightLessThan(840, Configuration.ORIENTATION_PORTRAIT) {
        itemOffset = 200.floatDp.toInt()
      }
      .heightLessThan(480, Configuration.ORIENTATION_LANDSCAPE) {
        itemOffset = 300.floatDp.toInt()
      }
      .widthLessThan(840, Configuration.ORIENTATION_LANDSCAPE) {
        itemOffset = 260.floatDp.toInt()
      }.execute()

    val itemDistance = 24.floatDp.toInt()
    val sideDisplayWidth = itemOffset - itemDistance
    val pageTranslationX = sideDisplayWidth + itemOffset

    binding.vpQuestionCard.apply {
      adapter = QuestionHistoryAdapter()
      addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
          outRect: Rect,
          view: View,
          parent: RecyclerView,
          state: RecyclerView.State
        ) {
          outRect.right = itemOffset
          outRect.left = itemOffset
        }
      })
      offscreenPageLimit = 3
      setPageTransformer { page, position ->
        page.translationX = -pageTranslationX * position
      }
    }

    TabLayoutMediator(binding.tlDot, binding.vpQuestionCard) { _, _ -> }.attach()
  }

  private fun setOnBackButtonPress() {
    val callback = object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        val key = viewModel.viewState.value.quizId
        activityViewModel.savedStateHandle[key] = binding.vpQuestionCard.currentItem
        viewModel.sendEvent(QuizHistoryDetailEvent.OnClickBackButton)
      }
    }
    activity?.onBackPressedDispatcher?.addCallback(this, callback)
  }

  private fun loadPrevPageState() {
    val quizId = arguments?.getString("quizId") ?: ""
    if (activityViewModel.savedStateHandle.contains(quizId)) {
      activityViewModel.savedStateHandle.get<Int>(quizId)?.let {
        binding.vpQuestionCard.setCurrentItem(it, false)
      }
    }
  }

  private fun configureForDisplay() {
    val tvQuizTypeId = binding.tvQuizType?.id ?: return
    ConfigurationForDisplay(context)
      .widthLessThan(480, Configuration.ORIENTATION_PORTRAIT) {
        binding.tvQuizCorrectCount?.layoutParams.apply {
          if (this !is ConstraintLayout.LayoutParams) return@apply
          this.startToStart = binding.glStart.id
          this.baselineToBaseline = -1
          this.topToBottom = tvQuizTypeId
        }
      }
      .heightLessThan(840, Configuration.ORIENTATION_PORTRAIT) {
        binding.tvQuizCorrectCount?.layoutParams.apply {
          if (this !is ConstraintLayout.LayoutParams) return@apply
          this.startToEnd = tvQuizTypeId
          this.baselineToBaseline = tvQuizTypeId
        }
      }.execute()
  }
}