package com.fikrielg.dictionarypocket.di

import android.content.Context
import androidx.room.Room
import com.fikrielg.dictionarypocket.BuildConfig
import com.fikrielg.dictionarypocket.data.repository.DictionaryRepository
import com.fikrielg.dictionarypocket.data.repository.DictionaryRepositoryImpl
import com.fikrielg.dictionarypocket.data.source.local.HistoryDatabase
import com.fikrielg.dictionarypocket.data.source.remote.ApiInterface
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.gotrue.SessionSource
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHistoryDatabase(
        @ApplicationContext context: Context
    ): HistoryDatabase {
        return Room.databaseBuilder(
            context,
            HistoryDatabase::class.java,
            "history.db"
        ).build()
    }




    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.DICTIONARY_POCKET_SUPABASE_URL,
            supabaseKey = BuildConfig.DICTIONARY_POCKET_SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Auth)
        }
    }

//    @Provides
//    @Singleton
//    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
//        return client.postgrest
//    }
//
//    @Provides
//    @Singleton
//    fun provideSupabaseAuth(client: SupabaseClient): Auth {
//        return client.auth
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideSupabaseStorage(client: SupabaseClient): Storage {
//        return client.storage
//    }

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun loggingInterceptor(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(
            BuildConfig.API_DICTIONARY_BASE_URL
        ).addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(loggingInterceptor())
        .build()

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideDictionaryRepository(
        api: ApiInterface,
        client: SupabaseClient,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DictionaryRepository {
        return DictionaryRepositoryImpl(
            api = api,
            client = client,
            ioDispatcher = ioDispatcher
        )
    }
}

@Retention
@Qualifier
annotation class IoDispatcher


