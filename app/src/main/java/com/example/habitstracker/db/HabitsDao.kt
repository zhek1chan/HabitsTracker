package com.example.habitstracker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HabitsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pl: HabitEntity)

    @Query("SELECT * FROM habits_table")
    fun getAll(): List<HabitEntity>

    @Query("DELETE FROM habits_table WHERE id = :id")
    fun delete(id: Int)
}
