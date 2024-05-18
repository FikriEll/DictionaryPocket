package com.fikrielg.dictionarypocket.data.repository


import com.fikrielg.dictionarypocket.data.kotpref.AuthPref
import com.fikrielg.dictionarypocket.data.source.local.HistoryDatabase
import com.fikrielg.dictionarypocket.data.source.local.entities.History
import com.fikrielg.dictionarypocket.data.source.remote.ApiInterface
import com.fikrielg.dictionarypocket.data.source.remote.model.DictionaryResponse
import com.fikrielg.dictionarypocket.data.source.remote.model.Bookmark
import com.haroldadmin.cnradapter.NetworkResponse
import com.rmaprojects.apirequeststate.ResponseState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresListDataFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class DictionaryRepositoryImpl @Inject constructor(
    private val api: ApiInterface,
    private val client: SupabaseClient,
    private val historyDatabase: HistoryDatabase,
) : DictionaryRepository {
    override suspend fun getDefinition(word: String): Flow<ResponseState<List<DictionaryResponse>>> =
        flow {
            emit(ResponseState.Loading)
            when (val response = api.getDefinition(word = word)) {
                is NetworkResponse.Success -> {
                    val dictionaryResponse = response.body
                    emit(ResponseState.Success(data = dictionaryResponse))
                }

                is NetworkResponse.ServerError -> {
                    emit(
                        ResponseState.Error(
                            message = response.body?.message ?: "Try again with a new word!"
                        )
                    )
                }

                is NetworkResponse.NetworkError -> {
                    emit(
                        ResponseState.Error(
                            message = "Please check if you're connected to the internet or try again later"
                        )
                    )
                }

                is NetworkResponse.UnknownError -> {
                    emit(ResponseState.Error(message = "Unknown error occurred while fetching definition"))
                }
            }
        }

    override fun getHistoryList(): Flow<List<History>> {
        return historyDatabase.historyDao().getHistoryList()
    }

    override suspend fun addHistory(history: History) {
        return historyDatabase.historyDao().insertHistory(history)
    }

    override suspend fun deleteHistory(history: History) {
        return historyDatabase.historyDao().deleteHistory(history)
    }

    private val dictionaryPocketChannel = client.channel("dictionarypocket")

    @OptIn(SupabaseExperimental::class)
    override suspend fun getBookmarkList(): Result<Flow<List<Bookmark>>> {
        val data = dictionaryPocketChannel.postgresListDataFlow(
            schema = "public",
            table = "bookmarks",
            primaryKey = Bookmark::id,
            filter = FilterOperation("user_id", FilterOperator.EQ, AuthPref.id)
        ).flowOn(Dispatchers.IO)

        dictionaryPocketChannel.subscribe()

        return Result.success(data)
    }


    override fun addBookmark(bookmark: Bookmark): Flow<ResponseState<Boolean>> =
        flow {
            emit(ResponseState.Loading)
            try {
                val currentUser = client.auth.currentUserOrNull()
                if (currentUser != null) {
                    val existingBookmark =
                        client.postgrest["bookmarks"]
                            .select(Columns.list("word")) {
                                filter {
                                    and {
                                        Bookmark::word eq bookmark.word
                                        Bookmark::userId eq AuthPref.id
                                    }

                                }
                            }.decodeSingleOrNull<Bookmark>()
                    if (existingBookmark != null) {
                        emit(ResponseState.Error("Bookmark Already Exists"))
                    } else {
                        client.postgrest["bookmarks"].insert(bookmark)
                        emit(ResponseState.Success(true))
                    }
                } else {
                    emit(ResponseState.Error("You haven't logged in, please log in"))
                }
            } catch (e: Exception) {
                emit(ResponseState.Error(e.message.toString()))
            }
        }

    override suspend fun unsubscribeChannel() {
        dictionaryPocketChannel.unsubscribe()
        client.realtime.removeChannel(dictionaryPocketChannel)
    }

    override fun deleteBookmark(id: String): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        try {
            ResponseState.Success(
                client.from("bookmarks").delete(
                    request = {
                        filter {
                            eq("id", id)
                        }
                    })
            )
            emit(ResponseState.Success(true))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message.toString()))
        }
    }


    override fun deleteAllBookmark(): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        try {
            ResponseState.Success(
                client.from("bookmarks").delete {
                    filter {
                        Bookmark::userId eq AuthPref.id
                    }
                }
            )
        } catch (e: Exception) {
            ResponseState.Error(e.message.toString())
        }
    }

}