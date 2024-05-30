package com.example.habitstracker.domain.usecase

import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.domain.repository.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PutHabitToRemoteUseCase(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun putHabitToRemote(habit: HabitEntity) = withContext(dispatcher) {
        habitsRepository.putHabitToRemote(habit)
    }

}