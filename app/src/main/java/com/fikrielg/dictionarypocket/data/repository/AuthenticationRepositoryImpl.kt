package com.fikrielg.dictionarypocket.data.repository

import android.util.Log
import com.fikrielg.dictionarypocket.data.kotpref.AuthPref
import com.fikrielg.dictionarypocket.data.source.remote.model.Users
import com.fikrielg.dictionarypocket.util.Resource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val client: SupabaseClient
) : AuthenticationRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = flow {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        val user = client.auth.currentSessionOrNull()?.user
        val publicUser = client.postgrest["users"].select {
            filter {
                Users::id eq (user?.id ?: 0)
            }
        }.decodeList<Users>()

        AuthPref.apply {
            username = publicUser[0].username
        }
    }


    override suspend fun signUp(
        email: String,
        password: String,
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            emit(Resource.Success(true))
        } catch(e: Exception) {
            Log.d("ERROR SIGN UP", e.message.toString())
            emit(Resource.Error(e.message.toString() ?: "Error Ocurred"))
        }
    }

}
