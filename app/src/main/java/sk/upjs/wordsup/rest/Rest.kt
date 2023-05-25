package sk.upjs.wordsup.rest

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import sk.upjs.wordsup.dao.Word

private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"

private val MOSHI = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val RETROFIT = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(MOSHI))
    .build()

interface WordsRestDao {

    @GET("{word}")
    suspend fun getWordDefinition(@Path("word") word: String) : List<Word>
}

object RestApi {
    val wordsRestDao : WordsRestDao by lazy {
        RETROFIT.create(WordsRestDao::class.java)
    }
}
