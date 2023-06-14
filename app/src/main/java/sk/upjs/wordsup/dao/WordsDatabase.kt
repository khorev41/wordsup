package sk.upjs.wordsup.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.upjs.wordsup.dao.quiz.Quiz
import sk.upjs.wordsup.dao.quiz.QuizDao
import sk.upjs.wordsup.dao.quiz.QuizWordCrossRef
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.word.WordsDao

@Database(entities = [Word::class, Quiz::class, Try::class, QuizWordCrossRef::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WordsDatabase : RoomDatabase() {

    abstract fun wordsDao(): WordsDao
    abstract fun quizDao(): QuizDao
    abstract fun triesDao(): TriesDao
}
