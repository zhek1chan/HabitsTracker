package com.example.habitstracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habitstracker.domain.usecase.GetHabitByIdUseCase
import com.example.habitstracker.domain.usecase.InsertHabitUseCase
import com.example.habitstracker.domain.usecase.PutHabitToRemoteUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitUseCase

class AddHabitViewModelFactory (
    private val insert : InsertHabitUseCase,
    private val put : PutHabitToRemoteUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddHabitViewModel(
            insert,
            put
        ) as T
    }
}
