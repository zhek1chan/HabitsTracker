package com.example.habitstracker.ui.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.domain.models.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddHabitViewModel : ViewModel() {

    private val db = AppDataBase
    private val convertor = HabitMapper()
    fun deleteItem(id: Int, context: Context) {
        viewModelScope.launch {
            db(context).habitDao().delete(id)
        }
    }

    fun addItem(item: Habit, context: Context) {
        viewModelScope.launch {
            db(context).habitDao().insert(convertor.map(item))
        }
    }

    fun updateItem(item: Habit, context: Context) {
        viewModelScope.launch {
            db(context).habitDao().update(convertor.map(item))
        }
    }
}