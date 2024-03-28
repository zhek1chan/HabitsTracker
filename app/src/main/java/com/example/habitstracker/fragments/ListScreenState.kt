package com.example.habitstracker.fragments

import com.example.habitstracker.Habit

sealed class ListScreenState {
    data object BadHabits : ListScreenState()
    data object GoodHabits : ListScreenState()
    data object NoHabitsAdded : ListScreenState()
    data class Data(var data: List<Habit>) : ListScreenState()
}
