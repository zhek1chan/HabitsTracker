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
    fun getAll(): LiveData<List<HabitEntity>>

    @Query("DELETE FROM habits_table WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM habits_table WHERE uid = :id")
    suspend fun deleteSame(id: String)

    @Update
    suspend fun update(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHabits(habits: List<HabitEntity>)

    @Query("SELECT * FROM habits_table")
    suspend fun getNotActualHabits() : List<HabitEntity>
}
