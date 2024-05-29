package com.example.habitstracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [HabitEntity::class]
)

abstract class AppDataBase : RoomDatabase() {
    abstract fun habitDao(): HabitsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null
        private var LOCK = Any()
        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context): AppDataBase =
            Room.databaseBuilder(
                context.applicationContext, AppDataBase::class.java, "app-database"
            )
                .allowMainThreadQueries()
                .build()
    }
}