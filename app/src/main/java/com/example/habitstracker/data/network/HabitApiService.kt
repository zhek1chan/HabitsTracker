package com.example.habitstracker.data.network

import com.example.habitstracker.domain.models.Uid
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface HabitApiService {

    @GET("/api/habit")
    suspend fun getHabits(): Response<List<HabitWithoutEnum>>

    @PUT("/api/habit")
    suspend fun putHabit(@Body habit: HabitWithoutEnum): Response<PutHabitResponse>

    @DELETE("/api/habit")
    suspend fun deleteHabit(@Body id: Uid): Response<Unit>
}