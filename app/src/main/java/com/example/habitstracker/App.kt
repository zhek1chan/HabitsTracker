package com.example.habitstracker

import android.app.Application
import com.example.habitstracker.data.workers.ActualizeRemoteWorker

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        actualizeRemote()
    }

    fun actualizeRemote() {
        ActualizeRemoteWorker.actualizeRemote()
    }
}