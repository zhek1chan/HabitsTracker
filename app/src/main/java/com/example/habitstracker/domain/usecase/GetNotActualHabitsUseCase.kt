package com.example.habitstracker.domain.usecase

import com.example.habitstracker.domain.repository.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetNotActualHabitsUseCase(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getNotActualHabits() = withContext(dispatcher) {
        habitsRepository.getNotActualHabits()
    }

}