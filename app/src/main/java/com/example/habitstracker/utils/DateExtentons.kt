package com.example.habitstracker.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate() = Calendar.getInstance(Locale.getDefault()).timeInMillis

fun getDateDefaultFormatted(date: Long): String = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(date)