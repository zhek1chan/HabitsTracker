package com.example.habitstracker.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Habit(
    var id: Int?,
    var title: String,
    var description: String,
    val type: Int, //enum
    val priority: Int, //enum
    var color: Int,
    var frequency: Int,
    var count: Int
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val habit = other as Habit
        return title == habit.title
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }
}