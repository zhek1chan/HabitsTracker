package com.example.habitstracker.db

import com.example.habitstracker.Habit

class HabitConvertor {
    fun map(h: Habit): HabitEntity {
        return HabitEntity(
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