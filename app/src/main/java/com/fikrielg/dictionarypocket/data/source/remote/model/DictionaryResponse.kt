package com.fikrielg.dictionarypocket.data.source.remote.model

data class DictionaryResponse(
    val meanings: List<Meaning>? = null,
    val origin: String? = null,
    val phonetic: String? = null,
    val phonetics: List<Phonetic>? = null,
    val word: String? = null
)

data class Meaning(
    val definitions: List<Definition>? = null,
    val partOfSpeech: String? = null
)

data class Definition(
    val antonyms: List<Any>? = null,
    val definition: String? = null,
    val example: String? = null,
    val synonyms: List<Any>? = null
)

data class Phonetic(
    val audio: String? = null,
    val text: String? = null
)