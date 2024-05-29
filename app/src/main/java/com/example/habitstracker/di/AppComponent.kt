package com.example.habitstracker.di

import com.example.habitstracker.domain.usecase.PutHabitToRemoteUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitsFromRemoteUseCase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, DataModule::class, DomainModule::class])
interface AppComponent {
    fun getUpdateHabitsFromRemoteUseCase(): UpdateHabitsFromRemoteUseCase

    fun getPutHabitToRemoteUseCase(): PutHabitToRemoteUseCase

    fun getUpdateHabitUseCase(): UpdateHabitUseCase
}