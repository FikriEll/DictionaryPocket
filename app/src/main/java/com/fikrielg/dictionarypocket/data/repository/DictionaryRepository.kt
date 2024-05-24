package com.fikrielg.dictionarypocket.data.repository

import com.fikrielg.dictionarypocket.data.source.local.entities.History
import com.fikrielg.dictionarypocket.data.source.remote.model.DictionaryResponse
import com.fikrielg.dictionarypocket.data.source.remote.model.Bookmark
import com.rmaprojects.apirequeststate.ResponseState
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun getDefinition(word: String): Flow<ResponseState<List<DictionaryResponse>>>
    fun getHistoryList(): Flow<List<History>>
    suspend fun addHistory(history: History)
    suspend fun deleteHistory(history: History)
    suspend fun deleteAllHistory()
    suspend fun getBookmarkList(): Result<Flow<List<Bookmark>>>
    fun addBookmark(bookmarks: Bookmark): Flow<ResponseState<Boolean>>
    suspend fun unsubscribeChannel()
    fun deleteBookmark(id: String): Flow<ResponseState<Boolean>>
    fun deleteAllBookmark(): Flow<ResponseState<Boolean>>
}