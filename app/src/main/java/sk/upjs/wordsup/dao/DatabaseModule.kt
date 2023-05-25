package sk.upjs.wordsup.dao

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class WordsApplication : Application()

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideWordsDao(database: WordsDatabase): WordsDao {
        return database.wordsDao()
    }

    @Provides
    fun provideQuizDao(database: WordsDatabase): QuizDao {
        return database.quizDao()
    }
    @Provides
    fun provideTriesDao(database: WordsDatabase): TriesDao {
        return database.triesDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): WordsDatabase {
        return Room.databaseBuilder(
            appContext,
            WordsDatabase::class.java,
            "words_database"
        ).build()
    }

}
