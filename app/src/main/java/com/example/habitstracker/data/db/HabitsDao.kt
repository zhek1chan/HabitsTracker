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
    fun insert(pl: HabitEntity)

    @Query("SELECT * FROM habits_table")
    fun getAll(): LiveData<List<HabitEntity>>

    @Query("DELETE FROM habits_table WHERE id = :id")
    fun delete(id: Int)

    @Update
    fun update(habit: HabitEntity)
}
