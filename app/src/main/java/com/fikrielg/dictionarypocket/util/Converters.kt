package com.fikrielg.dictionarypocket.util

import androidx.room.TypeConverter
import com.fikrielg.dictionarypocket.data.source.remote.Meaning
import com.fikrielg.dictionarypocket.data.source.remote.Phonetic
import com.google.gson.Gson


class Converters {

    @TypeConverter
    fun listMeaningToJson(value: List<Meaning>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToListMeaning(value: String) = Gson().fromJson(value, Array<Meaning>::class.java).toList()

    @TypeConverter
    fun listPhoneticToJson(value: List<Phonetic>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToListPhonetic(value: String) = Gson().fromJson(value, Array<Phonetic>::class.java).toList()
}