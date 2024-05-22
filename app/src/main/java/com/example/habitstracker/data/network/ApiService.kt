package com.example.habitstracker.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PUT

interface HabitApiService {

    @GET("/api/habit")
    suspend fun getHabits(): Response<List<HabitWithoutEnum>>

    @PUT("/api/habit")
    suspend fun putHabit(@Body habit: HabitWithoutEnum): Response<PutHabitResponse>

    @HTTP(method = "DELETE", path = "/api/habit", hasBody = true)
    suspend fun  deleteHabit(@Body uid: String)
}