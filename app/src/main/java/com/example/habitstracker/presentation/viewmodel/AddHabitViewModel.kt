package com.example.habitstracker.presentation.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.App
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.domain.usecase.GetHabitByIdUseCase
import com.example.habitstracker.domain.usecase.InsertHabitUseCase
import com.example.habitstracker.domain.usecase.PutHabitToRemoteUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddHabitViewModel(
    private val insertUseCase: InsertHabitUseCase,
    private val putUseCase: PutHabitToRemoteUseCase
) : ViewModel() {

    private val db = AppDataBase
    private val convertor = HabitMapper()

    fun deleteItem(item: Habit, context: Context) {
        viewModelScope.launch {
            //DoubletappApi.habitApiService.deleteHabit(Uid(item.uid)) - не работает метод DELETE у сервера Doubletapp
            //db(context).habitDao().delete(item.id!!)
        }
    }

    fun addItem(item: Habit) {
        addHabit(convertor.map(item))
    }

    private fun addHabit(habit: HabitEntity) {
        viewModelScope.launch {
            var isInserted = false
            try {
                putUseCase
                    .putHabitToRemote(habit)?.let { uid ->
                        insertUseCase.insertHabit(habit.copy(uid = uid, isActual = true))
                        Log.d("AddHabitViewModel", "Saved in db")
                        isInserted = true
                    }
            } catch (e: Exception) {
                Log.d(TAG, "INSERT ERROR!", e)
            }
            if (!isInserted) {
                insertUseCase.insertHabit(habit.copy(isActual = false))
                Log.d("AddHabitViewModel", "Saved in db")
                App().actualizeRemote()
            }
        }
    }

    fun updateItem(item: Habit) {
        addHabit(convertor.map(item))
    }
}