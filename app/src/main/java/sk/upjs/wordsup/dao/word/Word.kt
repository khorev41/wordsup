package sk.upjs.wordsup.dao.word

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sk.upjs.wordsup.dao.quiz.QuizWordCrossRef
import java.io.Serializable

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val wordId: Long,
    val word: String
): Serializable {
    override fun toString(): String = word
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Word

        if (word != other.word) return false

        return true
    }

    override fun hashCode(): Int {
        return word.hashCode()
    }


}
@Dao
interface WordsDao {
    @Query("SELECT * FROM words")
    fun getWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(words: List<Word>)

    @Query("DELETE FROM words")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteWords(words: List<Word>)

    @Delete
    suspend fun deleteQuizWordCrossRefs(quizWordCrossRefs: List<QuizWordCrossRef>)

    @Query("DELETE FROM QuizWordCrossRef WHERE quizId = :quizId AND wordId IN (:words)")
    suspend fun deleteQuizWordCrossRef(quizId: Long, words: List<Long>)

}


