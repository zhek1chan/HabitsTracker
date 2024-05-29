package com.example.habitstracker.domain.usecase

import com.example.habitstracker.domain.repository.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateHabitsFromRemoteUseCase(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun updateHabitsFromRemote() = withContext(dispatcher) {
        habitsRepository.updateHabitsFromRemote()
    }

}