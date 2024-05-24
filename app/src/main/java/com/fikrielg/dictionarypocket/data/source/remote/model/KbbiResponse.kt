package com.fikrielg.dictionarypocket.data.source.remote.model


import com.google.gson.annotations.SerializedName

data class KbbiResponse(
    @SerializedName("arti")
    val arti: List<String>,
    @SerializedName("lema")
    val lema: String
)