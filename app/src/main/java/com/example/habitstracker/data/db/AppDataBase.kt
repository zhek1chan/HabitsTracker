package com.example.habitstracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi

@Database(
    version = 1,
    entities = [HabitEntity::class]
)

abstract class AppDataBase : RoomDatabase() {
    abstract fun habitDao(): HabitsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null


        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context) : AppDataBase {
            kotlinx.coroutines.internal.synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "habits_database"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}