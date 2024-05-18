package com.fikrielg.dictionarypocket.data.source.remote

import com.fikrielg.dictionarypocket.data.source.remote.model.DictionaryResponse
import com.fikrielg.dictionarypocket.data.source.remote.model.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("api/v2/entries/en/{word}")
    suspend fun getDefinition(
        @Path("word") word: String
    ): NetworkResponse<List<DictionaryResponse>, ErrorResponse>
}