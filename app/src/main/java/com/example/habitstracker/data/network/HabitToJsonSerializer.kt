package com.example.habitstracker.data.network

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class HabitToJsonSerializer : JsonSerializer<HabitWithoutEnum> {
    override fun serialize(
        habit: HabitWithoutEnum,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement = JsonObject().apply {

        addProperty("color", habit.color)
        addProperty("count", habit.count)
        addProperty("date", habit.lastModified)
        addProperty("description", habit.description)
        addProperty("frequency", habit.frequency)
        addProperty("priority", habit.priority)
        addProperty("title", habit.title)
        addProperty("type", habit.type)

        if (habit.uid.isNotBlank()) {
            addProperty("uid", habit.uid)
        }
    }
}