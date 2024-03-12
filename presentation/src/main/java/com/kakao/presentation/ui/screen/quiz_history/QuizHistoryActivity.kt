package com.kakao.presentation.ui.screen.quiz_history

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.presentation.R
import com.kakao.presentation.databinding.ActivityQuizHistoryBinding
import com.kakao.presentation.utility.ConnectionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuizHistoryActivity : AppCompatActivity() {

  private lateinit var binding: ActivityQuizHistoryBinding

  private val viewModel: QuizHistoryViewModel by viewModels()

  @Inject
  lateinit var connectionManager: ConnectionManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz_history)

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        connectionManager.connectionState.collect {
          if (it == ConnectionManager.LOST) finish()
        }
      }
    }
  }
}