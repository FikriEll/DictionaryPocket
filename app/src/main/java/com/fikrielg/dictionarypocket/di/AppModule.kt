package com.fikrielg.dictionarypocket.di

import android.content.Context
import androidx.room.Room
import com.fikrielg.dictionarypocket.BuildConfig
import com.fikrielg.dictionarypocket.data.repository.AuthenticationRepository
import com.fikrielg.dictionarypocket.data.repository.AuthenticationRepositoryImpl
import com.fikrielg.dictionarypocket.data.repository.DictionaryRepository
import com.fikrielg.dictionarypocket.data.repository.DictionaryRepositoryImpl
import com.fikrielg.dictionarypocket.data.repository.KbbiRepository
import com.fikrielg.dictionarypocket.data.repository.KbbiRepositoryImpl
import com.fikrielg.dictionarypocket.data.source.local.HistoryDatabase
import com.fikrielg.dictionarypocket.data.source.remote.DictionaryApiInterface
import com.fikrielg.dictionarypocket.data.source.remote.KbbiApiInterface
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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
            install(Realtime)
        }
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        client: SupabaseClient
    ) : AuthenticationRepository {
        return AuthenticationRepositoryImpl(client)
    }

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
    fun provideDictionaryApiInterface(): DictionaryApiInterface = Retrofit.Builder()
        .baseUrl(
            BuildConfig.API_DICTIONARY_BASE_URL
        ).addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(loggingInterceptor())
        .build()
        .create()

    @Singleton
    @Provides
    fun provideKKBIApiInterface(): KbbiApiInterface = Retrofit.Builder()
        .baseUrl(
            BuildConfig.API_KBBI_BASE_URL
        ).addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(loggingInterceptor())
        .build()
        .create()


    @Provides
    @Singleton
    fun provideDictionaryRepository(
        api: DictionaryApiInterface,
        client: SupabaseClient,
        historyDatabase: HistoryDatabase
    ): DictionaryRepository {
        return DictionaryRepositoryImpl(
            api = api,
            client = client,
            historyDatabase = historyDatabase
        )
    }

    @Provides
    @Singleton
    fun provideKBBIRepository(
        api: KbbiApiInterface,
    ): KbbiRepository {
        return KbbiRepositoryImpl(
            api = api,
        )
    }
}



