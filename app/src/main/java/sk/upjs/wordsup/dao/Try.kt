package sk.upjs.wordsup.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Entity(tableName = "tries")
data class Try(
    @PrimaryKey(autoGenerate = true)
    val tryId: Long,
    val quizId: Long,
    val correctWords: Int,
    val time: Date

    )

@Dao
interface TriesDao {

    @Query("SELECT * FROM tries")
    fun getTries(): Flow<List<Try>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tries: List<Try>)

    @Query("DELETE FROM tries")
    suspend fun deleteAll()

}


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}