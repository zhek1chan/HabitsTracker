package com.example.habitstracker.data.network

import com.example.habitstracker.data.db.HabitEntity
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DoubletappApi {
    private const val BASE_URL = "https://droid-test-server.doubletapp.ru/"
    private const val AUTH_TOKEN = "b2ea135e-b4cf-4ee9-8523-b2b71a68e7e0"

    private fun getRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val headersInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder().header(
                "Authorization",
                AUTH_TOKEN
            )
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headersInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(HabitWithoutEnum::class.java, HabitToJsonSerializer())
            .registerTypeAdapter(HabitEntity::class.java, HabitJsonDes())
            .create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()

    }

    val habitApiService: HabitApiService by lazy {
        getRetrofit().create(HabitApiService::class.java)
    }
}
