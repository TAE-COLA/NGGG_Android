package com.kakao.remote.source

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kakao.core.error.FireStoreError
import com.kakao.data.model.QuizDataModel
import com.kakao.data.source.QuizDataSource
import com.kakao.remote.model.QuizRemoteModel
import com.kakao.remote.model.mapper.asData
import com.kakao.remote.model.mapper.asRemote
import com.kakao.remote.source.UserDataSourceImpl.Companion.USER_REF_PATH
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizDataSourceImpl @Inject constructor(
  private val firestore: FirebaseFirestore
) : QuizDataSource {

  companion object {
    const val QUIZ_REF_PATH = "quiz"
    const val COLUMN_CREATED_AT = "created_at"
  }

  override suspend fun insertQuiz(kakaoId: String, quiz: QuizDataModel) {
    firestore
      .collection("$USER_REF_PATH/$kakaoId/$QUIZ_REF_PATH")
      .add(quiz.asRemote())
      .await()
  }

  override suspend fun getQuizHistory(
    kakaoId: String
  ): List<QuizDataModel> =
    firestore
      .collection("$USER_REF_PATH/$kakaoId/$QUIZ_REF_PATH")
      .orderBy(COLUMN_CREATED_AT, Query.Direction.DESCENDING)
      .get()
      .await()
      .map { it.toObject(QuizRemoteModel::class.java).asData(it.id) }

  override suspend fun getQuizById(kakaoId: String, quizId: String): QuizDataModel =
    firestore
      .collection("$USER_REF_PATH/$kakaoId/$QUIZ_REF_PATH")
      .document(quizId)
      .get()
      .await()
      .let { it.toObject(QuizRemoteModel::class.java)?.asData(id = it.id) ?: throw FireStoreError.InvalidQuiz }
}

