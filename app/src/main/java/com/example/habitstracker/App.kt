package com.example.habitstracker

import android.app.Application
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.data.network.DoubletappApi
import com.example.habitstracker.data.network.HabitEntityMapper
import com.example.habitstracker.data.repository.Repository
import com.example.habitstracker.data.workers.ActualizeRemoteWorker
import com.example.habitstracker.di.AppComponent
import com.example.habitstracker.di.ContextModule
import com.example.habitstracker.di.DaggerAppComponent
import com.example.habitstracker.di.DomainModule
import kotlinx.coroutines.Dispatchers

class App: Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(this))
            .domainModule(
                DomainModule(
                    Repository(
                        AppDataBase.INSTANCE!!,
                        DoubletappApi(),
                        HabitMapper(),
                        HabitEntityMapper()
                    ),
                    Dispatchers.IO
                )
            )
            .build()

        actualizeRemote()
    }

    fun actualizeRemote() {
        ActualizeRemoteWorker.actualizeRemote()
    }
}