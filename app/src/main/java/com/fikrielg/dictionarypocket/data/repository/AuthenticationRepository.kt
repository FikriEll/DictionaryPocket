package com.fikrielg.dictionarypocket.data.repository

import com.rmaprojects.apirequeststate.ResponseState
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Flow<ResponseState<Boolean>>
    suspend fun signUp(username : String, email: String, password: String):  Flow<ResponseState<Boolean>>
    suspend fun updateUsername(username: String) : Flow<ResponseState<Boolean>>
    suspend fun signOut() : Flow<ResponseState<Boolean>>
}