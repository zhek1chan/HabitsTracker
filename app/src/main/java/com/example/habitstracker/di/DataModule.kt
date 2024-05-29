package com.example.habitstracker.di

import android.content.Context
import androidx.room.Room
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.data.network.DoubletappApi
import com.example.habitstracker.data.network.HabitEntityMapper
import com.example.habitstracker.data.repository.Repository
import com.example.habitstracker.domain.repository.HabitsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    fun provideHabitsRepository(
        database: AppDataBase,
        api: DoubletappApi,
        mapper: HabitMapper,
        mapperEntity: HabitEntityMapper
    ): HabitsRepository {
        return Repository(database, api, mapper, mapperEntity)
    }

    @Singleton
    @Provides
    fun provideHabitsDatabase(context: Context): AppDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            "habits_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesHabitsApi(): DoubletappApi {
        return DoubletappApi()
    }
}