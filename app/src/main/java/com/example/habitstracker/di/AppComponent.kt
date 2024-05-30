package com.example.habitstracker.di

import com.example.habitstracker.domain.usecase.GetAllHabitsUseCase
import com.example.habitstracker.domain.usecase.InsertHabitUseCase
import com.example.habitstracker.domain.usecase.PutHabitToRemoteUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitsFromRemoteUseCase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, DataModule::class, DomainModule::class, AppModule::class])
interface AppComponent {
    fun getUpdateHabitsFromRemoteUseCase(): UpdateHabitsFromRemoteUseCase

    fun getPutHabitToRemoteUseCase(): PutHabitToRemoteUseCase

    fun getInsertHabitUseCase(): InsertHabitUseCase

    //fun getHabitByIdUseCase(): GetHabitByIdUseCase

    fun getAllHabitsUseCase(): GetAllHabitsUseCase
}