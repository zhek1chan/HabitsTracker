package com.example.habitstracker.domain.usecase

import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.domain.repository.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateHabitUseCase(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun updateHabit(habit: HabitEntity) = withContext(dispatcher) {
        habitsRepository.updateHabit(habit)
    }

}