package com.example.habitstracker.ui.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.domain.models.Habit

class AddHabitViewModel : ViewModel() {

    private val db = AppDataBase
    private val convertor = HabitMapper()
    suspend fun deleteItem(id: Int, context: Context) {
        db(context).habitDao().delete(id)
    }

    suspend fun addItem(item: Habit, context: Context) {
        db(context).habitDao().insert(convertor.map(item))
    }

    suspend fun updateItem(item: Habit, context: Context) {
        db(context).habitDao().update(convertor.map(item))
    }
}