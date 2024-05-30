package com.example.habitstracker.di

import com.example.habitstracker.domain.repository.HabitsRepository
import com.example.habitstracker.domain.usecase.GetAllHabitsUseCase
import com.example.habitstracker.domain.usecase.GetNotActualHabitsUseCase
import com.example.habitstracker.domain.usecase.InsertHabitUseCase
import com.example.habitstracker.domain.usecase.PutHabitToRemoteUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitsFromRemoteUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
class DomainModule(
    private val habitsRepository: HabitsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    @Provides
    fun provideGetAllHabitsUseCase() =
        GetAllHabitsUseCase(habitsRepository)

    @Provides
    fun provideGetNotActualHabitsUseCase() =
        GetNotActualHabitsUseCase(habitsRepository, dispatcher)


    @Provides
    fun providePutHabitToRemoteUseCase() =
        PutHabitToRemoteUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideUpdateHabitsFromRemoteUseCase() =
        UpdateHabitsFromRemoteUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideUpdateHabitUseCase() =
        UpdateHabitUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideInsertHabitUseCase() =
        InsertHabitUseCase(habitsRepository, dispatcher)
}