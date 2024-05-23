package com.example.habitstracker

import android.app.Application
import com.example.habitstracker.data.workers.ActualizeRemoteWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    fun actualizeRemote() {
        ActualizeRemoteWorker.actualizeRemote()
    }
}