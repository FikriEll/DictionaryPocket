package com.fikrielg.dictionarypocket.data.repository

import com.fikrielg.dictionarypocket.data.source.remote.model.KbbiResponse
import com.rmaprojects.apirequeststate.ResponseState
import kotlinx.coroutines.flow.Flow

interface KbbiRepository {
    suspend fun getMeaningsKbbi(kata: String): Flow<ResponseState<KbbiResponse>>

}