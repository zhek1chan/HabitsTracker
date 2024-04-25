package com.example.habitstracker.ui.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.domain.models.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddHabitViewModel : ViewModel() {

    private val db = AppDataBase
    private val convertor = HabitMapper()
    suspend fun deleteItem(id: Int, context: Context) {
        withContext(Dispatchers.Main) {
            db(context).habitDao().delete(id)
        }

    }

    suspend fun addItem(item: Habit, context: Context) {
        withContext(Dispatchers.Main) {
            db(context).habitDao().insert(convertor.map(item))
        }
    }

    suspend fun updateItem(item: Habit, context: Context) {
        withContext(Dispatchers.Main) {
            db(context).habitDao().update(convertor.map(item))
        }
    }
}