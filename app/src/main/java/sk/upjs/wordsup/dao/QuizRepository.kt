package sk.upjs.wordsup.dao

import javax.inject.Inject

class QuizRepository @Inject constructor(private val dao: QuizDao) {

    val quizzes = dao.getQuizzes()

    //TODO
//    @WorkerThread
//    suspend fun loadQuizzes() {
//        dao.deleteAll()
//        try {
//            val records = RestApi.namesRestDao.getNames()
//            dao.insert(records)
//        } catch (e: Exception) {
//            Log.e("REST", e.toString())
//        }
//    }

}