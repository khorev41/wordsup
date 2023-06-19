package sk.upjs.wordsup.dao.quiz

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sk.upjs.wordsup.dao.tries.Try
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
    @Delete
    suspend fun deleteWord(word: Word)

    suspend fun insertQuizWithWords(quiz: QuizWithWords) {
        var quizID = insertQuiz(quiz.quiz)
        if(quizID == -1L){
            quizID = quiz.quiz.quizId
        }
        quiz.words.forEach {
            if (it.wordId == 0L) {
                insertWord(it)
                insertQuizWordCrossRef(QuizWordCrossRef(getWord(it.word).wordId, quizID))
            } else {
                var wordID = insertWord(it)
                if(wordID == -1L){
                    wordID = it.wordId
                }
                insertQuizWordCrossRef(QuizWordCrossRef(wordID, quizID))
            }
        }
    }

    suspend fun deleteQuizWithWords(quiz: QuizWithWords) {
        deleteTriesByQuizId(quiz.quiz.quizId)

        quiz.words.forEach {
            deleteQuizWordCrossRef(QuizWordCrossRef(it.wordId, quiz.quiz.quizId))
        }
        deleteQuiz(quiz.quiz)

        deleteWordsIfPossible()

    }

    @Query("DELETE FROM words WHERE words.wordId NOT IN (SELECT q.wordId FROM QuizWordCrossRef q)")
    suspend fun deleteWordsIfPossible()

    @Query("DELETE FROM tries WHERE quizId = :quiz")
    suspend fun deleteTriesByQuizId(quiz: Long)

    @Query("SELECT * FROM tries WHERE quizId = :quiz")
    suspend fun getTriesByQuizId(quiz: Long) : List<Try>

    @Insert
    suspend fun insertTries(tries: List<Try>)

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Delete
    suspend fun deleteQuizWordCrossRef(quizWordCrossRef: QuizWordCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizWordCrossRef(quizWordCrossRef: QuizWordCrossRef)

    @Query("DELETE FROM quizzes")
    suspend fun deleteAll()


}
