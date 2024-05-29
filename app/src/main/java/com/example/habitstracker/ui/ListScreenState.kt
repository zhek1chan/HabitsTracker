package com.example.habitstracker.ui

import com.example.habitstracker.domain.models.Habit

sealed class ListScreenState {
    data object NoHabitsAdded : ListScreenState()
    data class Data(var data: List<Habit>) : ListScreenState()
    data object Success : ListScreenState()
    data class Error(val message: String?, val formErrorType: String): ListScreenState()
    data object Loading : ListScreenState()
}
