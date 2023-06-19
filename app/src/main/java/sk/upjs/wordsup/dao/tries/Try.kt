package sk.upjs.wordsup.dao.tries

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sk.upjs.wordsup.dao.quiz.Quiz
import sk.upjs.wordsup.dao.quiz.QuizWordCrossRef
import sk.upjs.wordsup.dao.word.Word
import java.util.*

@Entity(tableName = "tries",indices = [Index(value = ["quizId"])])
data class Try(
    @PrimaryKey(autoGenerate = true) val tryId: Long,
    val quizId: Long,
    val percentage: Int,
    val time: Date,
)

data class TryWithQuiz(
    @Embedded var attempt: Try,
    @Relation(
        parentColumn = "quizId",
        entityColumn = "quizId",
        associateBy = Junction(Try::class)
    )
    val quiz: Quiz,
    @Relation(
        parentColumn = "quizId",
        entityColumn = "wordId",
        associateBy = Junction(QuizWordCrossRef::class)
    )
    val words: List<Word>,
)

@Dao
interface TryDao {

    @Transaction
    @Query("SELECT * FROM tries")
    fun getTriesWithQuiz(): Flow<List<TryWithQuiz>>

    @Query("SELECT * FROM tries")
    fun getTries(): Flow<List<Try>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tries: List<Try>)

    @Query("DELETE FROM tries")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(tries: List<Try>)

}


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}