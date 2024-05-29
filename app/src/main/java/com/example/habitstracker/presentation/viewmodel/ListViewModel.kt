package com.example.habitstracker.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.data.workers.ActualizeDatabaseWorker
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.presentation.ListScreenState
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val db = AppDataBase
    private val convertor = HabitMapper()
    private val stateLiveData = MutableLiveData<ListScreenState>()
    fun observeState(): LiveData<ListScreenState> = stateLiveData
    fun fillData(context: Context, lifeCycleOwner: LifecycleOwner) {
        val list: MutableList<Habit> = mutableListOf()
        val liveDataDb: LiveData<List<HabitEntity>> = db(context).habitDao().getAll()
        updateHabitsFromRemote(context)
        liveDataDb.observe(lifeCycleOwner) { it ->
            it.forEach {
                val item = convertor.map(it)
                var duplicated = false
                list.forEach {
                    if (item == it) {
                        duplicated = true
                    } else if (item.uid == it.uid) {
                        duplicated = true
                    }
                }
                if (!duplicated) {
                    list.add(convertor.map(it))
                }
            }
            processResult(list)
        }
        
    }

    private fun processResult(habits: List<Habit>) {
        if (habits.isEmpty()) {
            stateLiveData.postValue(ListScreenState.NoHabitsAdded)
        } else {
            stateLiveData.postValue(ListScreenState.Data(habits))
        }
    }

    private fun updateHabitsFromRemote(context: Context) {
        viewModelScope.launch {
            stateLiveData.postValue(ListScreenState.Loading)

            Log.d("ListVM", "Update habits from server")

            if (ActualizeDatabaseWorker.updateHabitsFromRemote(context)) {
                stateLiveData.postValue(ListScreenState.Success)
            } else {
                stateLiveData.postValue(ListScreenState.Error(null, "Load error"))
                ActualizeDatabaseWorker.actualizeDatabase()
            }
        }
    }
}