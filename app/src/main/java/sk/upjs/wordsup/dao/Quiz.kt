package sk.upjs.wordsup.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val quizId: Int,
    val name: String,
    val wordsNumber: Int,
)

data class QuizWithWords(
    @Embedded val quiz: Quiz,
    @Relation(
        parentColumn = "quizId",
        entityColumn = "wordId",
        associateBy = Junction(QuizWordCrossRef::class)
    )
    val words: List<Word>
)

@Dao
interface QuizDao {

    @Transaction
    @Query("SELECT * FROM quizzes")
    fun getQuizWithWords(): List<QuizWithWords>

    @Query("SELECT * FROM quizzes")
    fun getQuizzes(): Flow<List<Quiz>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(quizzes: List<Quiz>)

    @Query("DELETE FROM quizzes")
    suspend fun deleteAll()

}
