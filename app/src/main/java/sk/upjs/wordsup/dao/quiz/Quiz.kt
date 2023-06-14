package sk.upjs.wordsup.dao.quiz

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sk.upjs.wordsup.dao.word.Word
import java.io.Serializable

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val quizId: Int,
    val name: String,
    ) : Serializable

@Entity(primaryKeys = ["quizId", "wordId"])
data class QuizWordCrossRef(
    val wordId: Int,
    val quizId: Int,
)
data class QuizWithWords(
    @Embedded val quiz: Quiz,
    @Relation(
        parentColumn = "quizId",
        entityColumn = "wordId",
        associateBy = Junction(QuizWordCrossRef::class)
    )
    val words: List<Word>
) : Serializable

@Dao
interface QuizDao {

    @Transaction
    @Query("SELECT * FROM quizzes")
    fun getQuizzesWithWords(): Flow<List<QuizWithWords>>

    @Query("SELECT * FROM quizzes")
    fun getQuizzes(): Flow<List<Quiz>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(quizzes: List<Quiz>)

    @Query("DELETE FROM quizzes")
    suspend fun deleteAll()


}
