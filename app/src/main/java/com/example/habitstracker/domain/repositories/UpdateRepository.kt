package com.example.habitstracker.domain.repositories

interface UpdateRepository<T> {
    suspend fun insert(data: T?)

    suspend fun update(data: T?)

}