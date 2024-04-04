package com.example.habitstracker.data.db

import com.example.habitstracker.domain.Habit

class HabitConvertor {
    fun map(h: Habit): HabitEntity {
        return HabitEntity(
            h.id!!,
            h.title,
            h.description,
            h.type,
            h.priority,
            h.color,
            h.frequency,
            h.count
        )
    }

    fun map(h: HabitEntity): Habit {
        return Habit(
            h.id,
            h.title,
            h.description,
            h.type,
            h.priority,
            h.color,
            h.frequency,
            h.count
        )
    }
}