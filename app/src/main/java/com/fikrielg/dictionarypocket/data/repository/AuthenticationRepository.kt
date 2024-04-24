package com.fikrielg.dictionarypocket.data.repository

import com.fikrielg.dictionarypocket.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Flow<Resource<Boolean>>
    suspend fun signUp(email: String, password: String):  Flow<Resource<Boolean>>
}