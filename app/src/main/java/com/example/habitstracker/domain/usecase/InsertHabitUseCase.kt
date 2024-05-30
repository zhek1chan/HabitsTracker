package com.example.habitstracker.domain.usecase

import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.domain.repository.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class InsertHabitUseCase(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun insertHabit(habit: HabitEntity) = withContext(dispatcher) {
        habitsRepository.insertHabit(habit)
    }

}