package com.example.habitstracker.data.dbrepository

import androidx.lifecycle.LiveData
import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.data.db.HabitsDao
import com.example.habitstracker.domain.repositories.DeleteRepository
import com.example.habitstracker.domain.repositories.GetRepository
import com.example.habitstracker.domain.repositories.UpdateRepository

class DbRepository(private val dao: HabitsDao) : UpdateRepository<HabitEntity>,
    GetRepository<String>, DeleteRepository<Int> {
    override suspend fun delete(data: Int?) {
        dao.delete(data!!)
    }

    override suspend fun deleteSame(data: Int?) {
        dao.deleteSame()
    }

    override suspend fun getAll(data: String?): List<HabitEntity> {
        return dao.getAll()
    }

    override suspend fun getNotActualHabits(data: String?): List<HabitEntity> {
        return dao.getNotActualHabits()
    }

    override suspend fun insert(data: HabitEntity?) {
        dao.insert(data!!)
    }

    override suspend fun update(data: HabitEntity?) {
        dao.update(data!!)
    }

}