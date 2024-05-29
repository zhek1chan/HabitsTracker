package com.example.habitstracker.domain.repository

import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.domain.models.Habit
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    fun getAllHabits(): Flow<List<Habit>>

    suspend fun getHabitById(id: Int): Habit

    suspend fun insertHabit(habit: HabitEntity)

    suspend fun updateHabit(habit: HabitEntity)

    suspend fun updateHabitsFromRemote(): Boolean

    suspend fun putHabitToRemote(habit: HabitEntity): String?

    suspend fun getNotActualHabits(): List<Habit>

}