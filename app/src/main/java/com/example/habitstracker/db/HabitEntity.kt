package com.example.habitstracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits_table")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val title: String,
    val description: String,
    val type: Int,
    val priority: Int,
    val color: Int,
    val frequency: Int,
    val count: Int
)