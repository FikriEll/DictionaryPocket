package com.fikrielg.dictionarypocket.data.repository

import com.fikrielg.dictionarypocket.data.source.local.HistoryDatabase
import com.fikrielg.dictionarypocket.data.source.local.entities.History
import com.fikrielg.dictionarypocket.data.source.remote.DictionaryResponseModel
import com.fikrielg.dictionarypocket.data.source.remote.ApiInterface
import com.fikrielg.dictionarypocket.data.source.remote.model.Bookmark
import com.fikrielg.dictionarypocket.di.IoDispatcher
import com.fikrielg.dictionarypocket.util.Resource
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


  class DictionaryRepositoryImpl @Inject constructor(
    private val api: ApiInterface,
    private val client: SupabaseClient,
    private val historyDatabase: HistoryDatabase,
     @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DictionaryRepository {

    override suspend fun getDefinition(word: String): Flow<Resource<List<DictionaryResponseModel>>> =
        flow {
            emit(Resource.Loading())
            when (val response = api.getDefinition(word = word)) {
                is NetworkResponse.Success -> {
                    val dictionaryResponse = response.body

                    emit(Resource.Success(data = dictionaryResponse))
                }
                is NetworkResponse.ServerError -> {
                    emit(
                        Resource.Error(
                            message = response.body?.message ?: "Try again with a new word!"
                        )
                    )
                }
                is NetworkResponse.NetworkError -> {
                    emit(
                        Resource.Error(
                            message = "Please check if you're connected to the internet or try again later"
                        )
                    )
                }
                is NetworkResponse.UnknownError -> {
                    emit(Resource.Error(message = "Unknown error occurred while fetching definition"))
                }
            }
        }.flowOn(ioDispatcher)

     override fun getHistoryList(): Flow<List<History>> {
         return historyDatabase.historyDao().getHistoryList()
     }

      override suspend fun addHistory(history: History) {
         return historyDatabase.historyDao().insertHistory(history)
     }

      override suspend fun deleteHistory(history: History) {
          return historyDatabase.historyDao().deleteHistory(history)
      }


  }