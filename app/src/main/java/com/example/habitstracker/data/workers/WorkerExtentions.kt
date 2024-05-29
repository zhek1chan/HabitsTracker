package com.example.habitstracker.data.workers

import java.util.Calendar

fun defaultDelay(): Long {
    val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()

    dueDate.add(Calendar.MINUTE, 1)

    if (dueDate.before(currentDate)) {
        dueDate.add(Calendar.MINUTE, 1)
    }

    return dueDate.timeInMillis - currentDate.timeInMillis
}