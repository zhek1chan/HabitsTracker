package com.example.habitstracker.domain.repositories

import androidx.lifecycle.LiveData
import com.example.habitstracker.data.db.HabitEntity

interface GetRepository<T> {
    suspend fun getAll(data: T?): List<HabitEntity>

    suspend fun getNotActualHabits(data: T?): List<HabitEntity>
}