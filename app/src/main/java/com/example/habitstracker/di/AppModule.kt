package com.example.habitstracker.di

import com.example.habitstracker.domain.usecase.GetAllHabitsUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitsFromRemoteUseCase
import com.example.habitstracker.presentation.viewmodel.ListViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideListViewModelFactory(
        getAllHabitsUseCase: GetAllHabitsUseCase,
        updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase,
    ) = ListViewModelFactory(
        getAllHabitsUseCase,
        updateHabitsFromRemoteUseCase
    )
}