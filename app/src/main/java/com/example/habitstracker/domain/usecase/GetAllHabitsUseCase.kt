package com.example.habitstracker.domain.usecase

import com.example.habitstracker.domain.repository.HabitsRepository

class GetAllHabitsUseCase(private val habitsRepository: HabitsRepository) {

    fun getAllHabits() = habitsRepository.getAllHabits()

}