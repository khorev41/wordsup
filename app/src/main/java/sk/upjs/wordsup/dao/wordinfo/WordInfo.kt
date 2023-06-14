package sk.upjs.wordsup.dao.wordinfo

import Phonetic
import java.io.Serializable

data class WordInfo(
val definitions: List<String>,
val phonetic: Phonetic
) : Serializable
