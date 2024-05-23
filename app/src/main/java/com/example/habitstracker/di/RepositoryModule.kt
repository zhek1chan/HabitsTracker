package com.example.habitstracker.di

import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.data.db.HabitsDao
import com.example.habitstracker.data.dbrepository.DbRepository
import com.example.habitstracker.domain.repositories.DeleteRepository
import com.example.habitstracker.domain.repositories.GetRepository
import com.example.habitstracker.domain.repositories.UpdateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDbRepository(dao: HabitsDao): DbRepository {
        return DbRepository(dao)
    }

    @Provides
    @Singleton
    fun provideGetRepository(vacancyRepo: DbRepository): GetRepository<List<HabitEntity>> {
        return vacancyRepo
    }

    @Provides
    @Singleton
    fun provideUpdateRepository(vacancyRepo: DbRepository): UpdateRepository<String> {
        return vacancyRepo
    }

    @Provides
    @Singleton
    fun provideDeleteRepository(vacancyRepo: DbRepository): DeleteRepository<String> {
        return vacancyRepo
    }
}