package com.example.habitstracker.ui.view_models

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.App
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.data.network.DoubletappApi
import com.example.habitstracker.data.network.HabitEntityMapper
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.domain.models.Uid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddHabitViewModel : ViewModel() {

    private val db = AppDataBase
    private val convertor = HabitMapper()
    private val convertorEntity = HabitEntityMapper()

    fun deleteItem(item: Habit, context: Context) {
        viewModelScope.launch {
            //DoubletappApi.habitApiService.deleteHabit(Uid(item.uid)) - не работает метод DELETE у сервера Doubletapp
            db(context).habitDao().delete(item.id!!)
        }
    }

    fun addItem(item: Habit, context: Context) {
        addHabit(convertor.map(item), context)
    }

    private fun addHabit(habit: HabitEntity, context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DoubletappApi.habitApiService.putHabit(convertorEntity.map(habit))
            }
            try {
                delay(2000L)
                withContext(Dispatchers.IO) {
                    var isInserted = false
                    try {
                        val response = DoubletappApi.habitApiService.putHabit(convertorEntity.map(habit))
                        if (response.isSuccessful) {
                            val putHabitResponse = response.body()

                            putHabitResponse?.let {
                                if (habit.uid.isNotEmpty()) {
                                    db(context).habitDao().insert(habit.copy(isActual = true))
                                } else {
                                    db(context).habitDao().insert(
                                        habit.copy(
                                            uid = putHabitResponse.uid,
                                            isActual = true
                                        )
                                    )
                                }
                                isInserted = true
                            }
                        } else {
                            response.errorBody()?.let {
                                throw Exception("Ошибка! Код: ${response.code()}, Текст: $it")
                            } ?: kotlin.run {
                                throw Exception("Ошибка! Код: ${response.code()}")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Ошибка", "INSERT ERROR!", e)
                    }

                    if (!isInserted) {
                        db(context).habitDao().insert(habit.copy(isActual = false))
                        App().actualizeRemote()
                    }
                }
            } catch (e: Exception) {
                //нужно по идее отловить
            }
        }
    }

    fun updateItem(item: Habit, context: Context) {
        addHabit(convertor.map(item), context)
    }
}