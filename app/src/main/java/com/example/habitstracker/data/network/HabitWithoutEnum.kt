package com.example.habitstracker.data.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HabitWithoutEnum(
    var id: Int,
    var uid: String,
    var title: String,
    var description: String,
    var type: Int,
    var priority: Int,
    var color: Int,
    var frequency: Int,
    var count: Int,
    var lastModified: Long,
    var lastModifiedDateTime: String?,
    var isActual: Boolean
) : Parcelable