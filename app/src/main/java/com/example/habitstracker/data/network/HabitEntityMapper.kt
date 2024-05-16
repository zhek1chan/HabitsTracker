package com.example.habitstracker.data.network

import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.domain.models.Priority
import com.example.habitstracker.domain.models.Type

class HabitEntityMapper {
    fun map(h: HabitEntity): HabitWithoutEnum {
        val type = if (h.type == Type.Good) {
            0
        } else 1
        val priority = if (h.priority == Priority.Low) {
            0
        } else if (h.priority == Priority.Medium) {
            1
        } else 2
        return HabitWithoutEnum(
            h.id!!,
            h.uid,
            h.title,
            h.description,
            type,
            priority,
            h.color,
            h.frequency,
            h.count,
            h.lastModified,
            h.date,
            h.isActual
        )
    }

    fun map(h: HabitWithoutEnum): HabitEntity {
        val type = if (h.type == 0) {
            Type.Good
        } else Type.Bad
        val priority = if (h.priority == 0) {
            Priority.Low
        } else if (h.priority == 1) {
            Priority.Medium
        } else Priority.Max
        return HabitEntity(
            h.id,
            h.uid,
            h.title,
            h.description,
            type,
            priority,
            h.color,
            h.frequency,
            h.count,
            h.lastModified,
            h.lastModifiedDateTime,
            h.isActual
        )
    }
}