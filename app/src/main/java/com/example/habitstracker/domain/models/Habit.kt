package com.example.habitstracker.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Habit(
    var id: Int?,
    var uid: String,
    var title: String,
    var description: String,
    val type: Type,
    val priority: Priority,
    var color: Int,
    var frequency: Int,
    var count: Int,
) : Parcelable