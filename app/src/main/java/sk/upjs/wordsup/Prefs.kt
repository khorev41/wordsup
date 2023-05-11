package sk.upjs.wordsup

import android.content.SharedPreferences
import android.content.Context


// singleton class
class Prefs private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
    }

    var name: String
        get() = sharedPreferences.getString(NAME, "") ?: ""
        set(value) = sharedPreferences.edit().putString(NAME, value).apply()

    companion object {

        private const val PREFS_FILE_NAME = "prefs"
        private const val NAME = "name"

        @Volatile
        private var instance: Prefs? = null

        fun getInstance(context: Context): Prefs {
            return instance ?: synchronized(this) {
                instance ?: Prefs(context.applicationContext).also { instance = it }
            }
        }
    }
}
