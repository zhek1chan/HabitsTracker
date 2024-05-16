package com.example.habitstracker.data.network

import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.domain.models.Priority
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitJsonDes : JsonDeserializer<HabitEntity> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ) = HabitEntity(
        uid = json.asJsonObject.get("uid").asString,
        title = json.asJsonObject.get("title").asString,
        description = json.asJsonObject.get("description").asString,
        priority = Priority.entries[json.asJsonObject.get("priority").asInt],
        type = com.example.habitstracker.domain.models.Type.entries[json.asJsonObject.get("type").asInt],
        count = json.asJsonObject.get("count").asInt,
        frequency = json.asJsonObject.get("frequency").asInt,
        color = json.asJsonObject.get("color").asInt,
        lastModified = json.asJsonObject.get("date").asLong,
        id = 0
    )
}