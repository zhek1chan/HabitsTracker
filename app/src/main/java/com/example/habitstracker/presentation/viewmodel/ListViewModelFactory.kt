package com.example.habitstracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habitstracker.domain.usecase.GetAllHabitsUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitsFromRemoteUseCase

class ListViewModelFactory(
    private val getAllHabitsUseCase: GetAllHabitsUseCase,
    private val updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListViewModel(
            getAllHabitsUseCase,
            updateHabitsFromRemoteUseCase,
        ) as T
    }
}