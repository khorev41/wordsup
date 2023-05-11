package sk.upjs.wordsup.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Word::class, Quiz::class, Try::class, QuizWordCrossRef::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WordsDatabase : RoomDatabase() {

    abstract fun wordsDao(): WordsDao
    abstract fun quizDao(): QuizDao
    abstract fun triesDao(): TriesDao
}
