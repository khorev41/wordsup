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

@Entity(primaryKeys = ["quizId", "wordId"])
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quizzes: List<Quiz>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizWithWords(quizWithWords: QuizWithWords) {
        insert(listOf(quizWithWords.quiz))
        insertWords(quizWithWords.words).forEach {
            insertQuizWordCrossRef(QuizWordCrossRef(it, quizWithWords.quiz.quizId))
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<Word>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizWordCrossRef(quizWordCrossRef: QuizWordCrossRef)

    @Query("DELETE FROM quizzes")
    suspend fun deleteAll()


}
