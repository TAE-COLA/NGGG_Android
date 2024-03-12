package com.kakao.presentation.utility

import com.kakao.core.PromptPack.splitWithDelimiters

internal fun submitAnswer(answer: String, answerList: List<String>, callback: (Boolean) -> Unit) =
  callback(answer.trim().splitWithDelimiters().sorted() == answerList.sorted())