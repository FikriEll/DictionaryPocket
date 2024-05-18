package com.fikrielg.dictionarypocket.data.repository

import android.util.Log
import com.fikrielg.dictionarypocket.data.kotpref.AuthPref
import com.fikrielg.dictionarypocket.data.source.remote.model.User
import com.rmaprojects.apirequeststate.ResponseState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val client: SupabaseClient
) : AuthenticationRepository {

    override suspend fun signIn(email: String, password: String): Flow<ResponseState<Boolean>> =
        flow {
            emit(ResponseState.Loading)
            try {
                client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                val user = client.auth.currentUserOrNull()
                val publicUser = client.from("users")
                    .select {
                        filter {
                            User::id eq user?.id
                        }
                    }.decodeSingle<User>()

                AuthPref.apply {
                    this.id = publicUser.id!!
                    this.username = publicUser.username
                    this.isLogin = true
                }

                emit(ResponseState.Success(true))
            } catch (e: Exception) {
                emit(ResponseState.Error(e.message.toString()))
                Log.d("Error Sign In", e.message.toString())

            }
        }

    override suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("username", username)
                }
            }

            val user = client.auth.currentUserOrNull()
            val publicUser = client.from("users")
                .select {
                    filter {
                        User::id eq user?.id
                    }
                }.decodeSingle<User>()

            AuthPref.apply {
                this.id = publicUser.id!!
                this.username = publicUser.username
                this.isLogin = true
            }
            emit(ResponseState.Success(true))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message.toString()))
            Log.d("Error Sign Up", e.message.toString())
        }
    }


    override suspend fun updateUsername(
        username: String,
    ): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        try {
            client.postgrest["users"].update({
                User::username setTo username
            }) {
                filter {
                    User::id eq AuthPref.id
                }
            }
            val publicUser = client.from("users")
                .select {
                    filter {
                        User::id eq AuthPref.id                    }
                }.decodeSingle<User>()
            AuthPref.apply {
                this.username = publicUser.username
            }
            emit(ResponseState.Success(true))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message.toString()))
            Log.d("Error Update Username", e.message.toString())
        }
    }

    override suspend fun signOut(): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading)
        try {
            client.auth.signOut()
            emit(ResponseState.Success(true))
        } catch (e: Exception){
            emit(ResponseState.Error(e.message.toString()))
        }
    }

}
