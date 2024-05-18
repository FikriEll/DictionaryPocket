package com.fikrielg.dictionarypocket.data.source.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fikrielg.dictionarypocket.data.source.remote.model.Meaning
import com.fikrielg.dictionarypocket.data.source.remote.model.Phonetic

@Entity(tableName = "history")
data class History(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo("meanings")
    val meanings: List<Meaning>? = emptyList(),
    @ColumnInfo("origin")
    val origin: String? = "",
    @ColumnInfo("phonetic")
    val phonetic: String? = "",
    @ColumnInfo("phonetics")
    val phonetics: List<Phonetic>? = emptyList(),
    @ColumnInfo("word")
    val word: String? = ""
)

