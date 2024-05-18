package com.fikrielg.dictionarypocket.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bookmark(
    @SerialName("word")
    val word: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("id")
    val id: Int? = null
)
