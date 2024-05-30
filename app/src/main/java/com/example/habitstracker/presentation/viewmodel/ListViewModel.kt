package com.example.habitstracker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.data.workers.ActualizeDatabaseWorker
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.domain.usecase.GetAllHabitsUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitsFromRemoteUseCase
import com.example.habitstracker.presentation.ListScreenState
import kotlinx.coroutines.launch

class ListViewModel(
    private val getAllHabitsUseCase: GetAllHabitsUseCase,
    private val updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase
) : ViewModel() {

    private val convertor = HabitMapper()
    private val stateLiveData = MutableLiveData<ListScreenState>()
    fun observeState(): LiveData<ListScreenState> = stateLiveData
    fun fillData(lifeCycleOwner: LifecycleOwner) {
        val list: MutableList<Habit> = mutableListOf()
        val liveDataDb: LiveData<List<HabitEntity>> =
            getAllHabitsUseCase.getAllHabits().asLiveData()
        updateHabitsFromRemote()
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

    private fun updateHabitsFromRemote() {
        viewModelScope.launch {
            stateLiveData.postValue(ListScreenState.Loading)

            Log.d("ListVM", "Update habits from server")

            if (updateHabitsFromRemoteUseCase.updateHabitsFromRemote()) {
                stateLiveData.postValue(ListScreenState.Success)
            } else {
                stateLiveData.postValue(ListScreenState.Error(null, "Load error"))
                ActualizeDatabaseWorker.actualizeDatabase()
            }
        }
    }
}