package sk.upjs.wordsup

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


// singleton class
class Prefs private constructor(context: Context) {

    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    var name: String
        get() = sharedPreferences.getString(NAME, "") ?: ""
        set(value) = sharedPreferences.edit().putString(NAME, value.trim()).apply()

    var target: Int
        get() = sharedPreferences.getInt(TARGET, 0)
        set(value) = sharedPreferences.edit().putInt(TARGET, value).apply()

    var learned: Int
        get() = sharedPreferences.getInt(LEARNED, 0)
        set(value) = sharedPreferences.edit().putInt(LEARNED, value).apply()

    var date: String
        get() = sharedPreferences.getString(DATE, "") ?: ""
        set(value) = sharedPreferences.edit().putString(DATE, value).apply()


    companion object {

        private const val NAME = "name"
        private const val TARGET = "target"
        private const val LEARNED = "learned"
        private const val DATE = "date"

        @Volatile
        private var instance: Prefs? = null

        fun getInstance(context: Context): Prefs {
            return instance ?: synchronized(this) {
                instance ?: Prefs(context.applicationContext).also { instance = it }
            }
        }
    }
}
