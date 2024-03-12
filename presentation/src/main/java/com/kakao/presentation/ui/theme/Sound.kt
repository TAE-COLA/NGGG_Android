package com.kakao.presentation.ui.theme

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.kakao.presentation.R

class SoundScheme(
  val correct: MediaPlayer = MediaPlayer(),
  val wrong: MediaPlayer = MediaPlayer()
) {
  fun release() {
    this.correct.release()
    this.wrong.release()
  }
}

internal val LocalMediaPlayer = staticCompositionLocalOf { SoundScheme() }

@Composable
fun SoundTheme(
  context: Context = LocalContext.current,
  content: @Composable () -> Unit
) {
  val soundScheme = SoundScheme(
    correct = MediaPlayer.create(context, R.raw.effect_correct),
    wrong = MediaPlayer.create(context, R.raw.effect_wrong)
  )

  DisposableEffect(Unit) {
    onDispose {
      soundScheme.release()
    }
  }

  CompositionLocalProvider(
    LocalMediaPlayer provides soundScheme,
    content = content
  )
}

object SoundTheme {
  val soundScheme: SoundScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalMediaPlayer.current
}