package com.fikrielg.dictionarypocket.data.source.remote

import com.fikrielg.dictionarypocket.data.source.remote.model.ErrorResponse
import com.fikrielg.dictionarypocket.data.source.remote.model.KbbiResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KbbiApiInterface {
    @GET("api/kbbi")
    suspend fun getMeaningKBBI(
        @Query("text") kata: String
    ): NetworkResponse<KbbiResponse, ErrorResponse>
}