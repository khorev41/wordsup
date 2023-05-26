package sk.upjs.wordsup.rest

import DefinitionResponseModelItem
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"

private val GSON = GsonBuilder().setLenient().create()

private val RETROFIT = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(GSON))
    .build()

interface WordsRestDao {

    @GET("{word}")
    suspend fun getDictionaryData(@Path("word") word: String) : List<DefinitionResponseModelItem>

}

object RestApi {
    val wordsRestDao : WordsRestDao by lazy {
        RETROFIT.create(WordsRestDao::class.java)
    }
}
