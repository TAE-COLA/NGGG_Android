package com.kakao.presentation.ui.screen.quiz_history.quiz_history_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kakao.presentation.R
import com.kakao.presentation.base.BaseFragment
import com.kakao.presentation.databinding.FragmentQuizHistoryListBinding
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_list.QuizHistoryListContract.QuizHistoryListEvent
import com.kakao.presentation.ui.screen.quiz_history.quiz_history_list.QuizHistoryListContract.QuizHistoryListSideEffect
import com.kakao.presentation.utility.StickyHeaderItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizHistoryListFragment :
  BaseFragment<FragmentQuizHistoryListBinding>(R.layout.fragment_quiz_history_list) {

  private val viewModel: QuizHistoryListViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    collectState()
    collectLoading()
    collectSideEffect()
    setEvents()
    initView()
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
            is QuizHistoryListSideEffect.PopBackStack -> activity?.finish()
            is QuizHistoryListSideEffect.NavigateToQuizHistoryDetail -> {
              val recyclerViewState = binding.rvQuizHistoryList.scrollState

              val action =
                QuizHistoryListFragmentDirections.actionQuizHistoryListFragmentToQuizHistoryDetailFragment(it.quiz.id)
              findNavController().navigate(action)
            }

            is QuizHistoryListSideEffect.ShowSnackbar -> {
              Snackbar.make(requireView(), it.message.asString(requireContext()), Snackbar.LENGTH_SHORT).show()
            }
          }
        }
      }
    }
  }

  private fun setEvents() {
    binding.apply {
      quizHistoryListToolbar.setNavigationOnClickListener { viewModel.sendEvent(QuizHistoryListEvent.OnClickBackButton) }
      onClickQuiz = { quiz, scrollState -> viewModel.sendEvent(QuizHistoryListEvent.OnClickQuiz(quiz, scrollState)) }
    }
  }

  private fun initView() {
    with(binding.rvQuizHistoryList) {
      adapter = QuizHistoryAdapter()
      addItemDecoration(
        StickyHeaderItemDecoration(object : StickyHeaderItemDecoration.SectionCallback {
          override fun isHeader(position: Int): Boolean {
            return (adapter as QuizHistoryAdapter).isFlagItem(position)
          }

          override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? {
            list.findViewHolderForAdapterPosition(position)
            return (adapter as QuizHistoryAdapter).getHeaderLayoutView(list, position)
          }
        }
        )
      )
    }
    val scrollState = viewModel.viewState.value.scrollState
    if (scrollState != null) {
      binding.rvQuizHistoryList.layoutManager?.onRestoreInstanceState(scrollState)
      binding.quizHistoryListAppBarLayout.setExpanded(false)
    }
  }
}