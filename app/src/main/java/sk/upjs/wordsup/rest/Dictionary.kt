//https://github.com/MelnikovAleksandr/DictionaryApp/tree/main/app/src/main/java/ru/asmelnikov/dictionaryapp/data/dto
data class Phonetic(
    val audio: String? = null,
    val text: String? = null
)

data class Definition(
    val antonyms: List<Any>? = null,
    val definition: String? = null,
    val example: String? = null,
    val synonyms: List<Any>? = null
)
data class Meaning(
    val definitions: List<Definition>? = null,
    val partOfSpeech: String? = null
)

data class ErrorResponse(
    val message: String,
    val resolution: String,
    val title: String
)

data class DefinitionResponseModelItem(
    val meanings: List<Meaning>? = null,
    val origin: String? = null,
    val phonetic: String? = null,
    val phonetics: List<Phonetic>? = null,
    val word: String? = null
)