package com.example.habitstracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.habitstracker.domain.models.Priority
import com.example.habitstracker.domain.models.Type
import com.example.habitstracker.utils.getCurrentDate
import com.example.habitstracker.utils.getDateDefaultFormatted

@Entity(tableName = "habits_table")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    val uid: String,
    val title: String,
    val description: String,
    val type: Type,
    val priority: Priority,
    val color: Int,
    val frequency: Int,
    val count: Int,
    val lastModified: Long = getCurrentDate(),
    val date: String? = getDateDefaultFormatted(lastModified),
    val isActual: Boolean = false
)