package com.example.habitstracker.ui

import com.example.habitstracker.domain.Habit

sealed class ListScreenState {
    data object NoHabitsAdded : ListScreenState()
    data class Data(var data: List<Habit>) : ListScreenState()
}
