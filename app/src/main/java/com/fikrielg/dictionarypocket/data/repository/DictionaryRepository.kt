package com.fikrielg.dictionarypocket.data.repository

import com.fikrielg.dictionarypocket.data.source.local.entities.History
import com.fikrielg.dictionarypocket.data.source.remote.DictionaryResponseModel
import com.fikrielg.dictionarypocket.data.source.remote.model.Bookmark
import com.fikrielg.dictionarypocket.util.Resource
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun getDefinition(word: String): Flow<Resource<List<DictionaryResponseModel>>>
    fun getHistoryList() : Flow<List<History>>
    suspend fun addHistory(history: History)
    suspend fun deleteHistory(history: History)
}