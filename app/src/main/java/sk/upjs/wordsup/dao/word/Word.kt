package sk.upjs.wordsup.dao.word

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.io.Serializable

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val wordId: Int,
    val word: String
): Serializable {
    override fun toString(): String = word
}
@Dao
interface WordsDao {
    @Query("SELECT * FROM words")
    fun getWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(words: List<Word>)

    @Query("DELETE FROM words")
    suspend fun deleteAll()

}


