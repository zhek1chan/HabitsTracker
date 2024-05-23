package com.example.habitstracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HabitsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pl: HabitEntity)

    @Query("SELECT * FROM habits_table")
    fun getAll(): List<HabitEntity>

    @Query("DELETE FROM habits_table WHERE id = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(habit: HabitEntity)

    @Query("SELECT * FROM habits_table")
    suspend fun getNotActualHabits() : List<HabitEntity>

    @Query("DELETE FROM habits_table WHERE isActual = 1")
    suspend fun deleteSame()
}
