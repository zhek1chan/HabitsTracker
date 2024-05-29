package com.example.habitstracker.domain.repositories

interface DeleteRepository<T> {
    suspend fun delete(data: T?)

    suspend fun deleteSame(data: T?)

}