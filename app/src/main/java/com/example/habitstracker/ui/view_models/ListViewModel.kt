package com.example.habitstracker.ui.view_models

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habitstracker.domain.Habit
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitConvertor
import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.ui.ListScreenState

class ListViewModel : ViewModel() {

    private val db = AppDataBase
    private val convertor = HabitConvertor()
    private val stateLiveData = MutableLiveData<ListScreenState>()
    fun observeState(): LiveData<ListScreenState> = stateLiveData
    fun fillData(context: Context) {
        val listFromDb: List<HabitEntity> = db(context).habitDao().getAll()
        val list: MutableList<Habit> = mutableListOf()
        listFromDb.forEach {
            list.add(convertor.map(it))
        }
        processResult(list)
    }


    private fun processResult(habits: List<Habit>) {
        Log.d("ListVM", "ProcessResult")
        if (habits.isEmpty()) {
            stateLiveData.postValue(ListScreenState.NoHabitsAdded)
        } else {
            stateLiveData.postValue(ListScreenState.Data(habits))
        }
    }

}