package com.fikrielg.dictionarypocket.data.repository

import com.fikrielg.dictionarypocket.data.source.remote.KbbiApiInterface
import com.fikrielg.dictionarypocket.data.source.remote.model.KbbiResponse
import com.haroldadmin.cnradapter.NetworkResponse
import com.rmaprojects.apirequeststate.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KbbiRepositoryImpl @Inject constructor(
    private val api: KbbiApiInterface,
) : KbbiRepository {
    override suspend fun getMeaningsKbbi(kata: String): Flow<ResponseState<KbbiResponse>>  = flow{
            emit(ResponseState.Loading)
            when (val response = api.getMeaningKBBI(kata = kata)) {
                is NetworkResponse.Success -> {
                    val kkbiResponse = response.body
                    emit(ResponseState.Success(data = kkbiResponse))
                }

                is NetworkResponse.ServerError -> {
                    emit(
                        ResponseState.Error(
                            message = response.body?.message ?: "Coba dengan kata baru!"
                        )
                    )
                }

                is NetworkResponse.NetworkError -> {
                    emit(
                        ResponseState.Error(
                            message = "Silakan periksa apakah Anda terhubung ke internet atau coba lagi nanti"
                        )
                    )
                }

                is NetworkResponse.UnknownError -> {
                    emit(ResponseState.Error(message = "Terjadi kesalahan yang tidak diketahui saat mengambil arti"))
                }
            }
        }

}