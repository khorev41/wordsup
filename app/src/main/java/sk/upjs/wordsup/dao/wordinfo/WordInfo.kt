package sk.upjs.wordsup.dao.wordinfo

import Phonetic
import sk.upjs.wordsup.dao.Word

data class WordInfo(

val word: String,
val definitions: List<String>,
val phonetic: Phonetic

)
