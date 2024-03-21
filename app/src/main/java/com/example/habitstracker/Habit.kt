package com.example.habitstracker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Habit(
    val id: Int?,
    val title: String,
    val description: String,
    val type: Int,
    val priority: Int,
    val color: Int,
    val frequency: Int,
    val count: Int
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