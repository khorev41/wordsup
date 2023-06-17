package sk.upjs.wordsup.dao.quiz

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sk.upjs.wordsup.dao.word.Word
import java.io.Serializable

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val quizId: Long,
    var name: String,
) : Serializable

@Entity(
    primaryKeys = ["quizId", "wordId"]
)
data class QuizWordCrossRef(
    val wordId: Long,
    val quizId: Long,
)
data class QuizWithWords(
    @Embedded var quiz: Quiz,
    @Relation(
        parentColumn = "quizId",
        entityColumn = "wordId",
        associateBy = Junction(QuizWordCrossRef::class)
    )
    var words: List<Word>,
) : Serializable

@Dao
interface QuizDao {

    @Transaction
    @Query("SELECT * FROM quizzes")
    fun getQuizzesWithWords(): Flow<List<QuizWithWords>>

    @Query("SELECT * FROM quizzes")
    fun getQuizzes(): Flow<List<Quiz>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuizzes(quizzes: List<Quiz>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuiz(quiz: Quiz): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWord(words: Word): Long

    @Query("SELECT * FROM words WHERE word = :w")
    suspend fun getWord(w: String): Word

    suspend fun insertQuizWithWords(quiz: QuizWithWords) {
        var quizID = quiz.quiz.quizId
        if (quiz.quiz.quizId == 0L) {
            quizID = insertQuiz(quiz.quiz)
        }
        quiz.words.forEach {
            if (it.wordId == 0L) {
                insertWord(it)
                insertQuizWordCrossRef(QuizWordCrossRef(getWord(it.word).wordId,quizID))
            }else{
                insertQuizWordCrossRef(QuizWordCrossRef(it.wordId,quizID))
            }
        }
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizWordCrossRef(quizWordCrossRef: QuizWordCrossRef)

    @Query("DELETE FROM quizzes")
    suspend fun deleteAll()


}
