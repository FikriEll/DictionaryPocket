package com.fikrielg.dictionarypocket.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName ("id")
    val id: String? = null,
    @SerialName ("username")
    val username: String,
    @SerialName ("created_at")
    val createdAt: String? = null
)
