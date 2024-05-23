package com.example.habitstracker.di

import android.content.Context
import androidx.room.Room
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.data.db.HabitsDao
import com.example.habitstracker.data.network.HabitApiService
import com.example.habitstracker.data.network.HabitJsonDes
import com.example.habitstracker.data.network.HabitToJsonSerializer
import com.example.habitstracker.data.network.HabitWithoutEnum
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase =
        Room.databaseBuilder(context, AppDataBase::class.java, "gtj_database.db").build()

    @Provides
    @Singleton
    fun provideVacancyDao(appDatabase: AppDataBase): HabitsDao =
        appDatabase.habitDao()

    @Provides
    @Singleton
    fun provideDoubletappService(): HabitApiService {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                chain.run {
                    proceed(
                        request()
                            .newBuilder()
                            .addHeader(
                                "Authorization",
                                "b2ea135e-b4cf-4ee9-8523-b2b71a68e7e0"
                            )
                            .build()
                    )
                }
            }
            .build()
        val gson = GsonBuilder()
            .registerTypeAdapter(HabitWithoutEnum::class.java, HabitToJsonSerializer())
            .registerTypeAdapter(HabitEntity::class.java, HabitJsonDes())
            .create()
        return Retrofit.Builder()
            .baseUrl("https://droid-test-server.doubletapp.ru/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(HabitApiService::class.java)
    }

}
