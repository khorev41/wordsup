package sk.upjs.wordsup.dao

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "quiz_word",
    primaryKeys = ["wordId", "quizId"],
    foreignKeys = [
        ForeignKey(entity = Word::class, parentColumns = ["wordId"], childColumns = ["wordId"]),
        ForeignKey(entity = Quiz::class, parentColumns = ["quizId"], childColumns = ["quizId"])
    ]
)
data class QuizWordCrossRef(

    val wordId: Int,
    val quizId: Int
)
