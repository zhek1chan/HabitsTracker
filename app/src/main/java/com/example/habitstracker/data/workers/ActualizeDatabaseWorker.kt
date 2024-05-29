package com.example.habitstracker.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.habitstracker.App
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.data.network.DoubletappApi
import com.example.habitstracker.data.network.HabitEntityMapper
import com.example.habitstracker.data.repository.Repository
import com.example.habitstracker.domain.usecase.UpdateHabitsFromRemoteUseCase
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class ActualizeDatabaseWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: START")

        return if (UpdateHabitsFromRemoteUseCase(Repository(AppDataBase.INSTANCE!!, DoubletappApi(), HabitMapper(), HabitEntityMapper()), Dispatchers.IO)
                .updateHabitsFromRemote()) {
            Result.success()
        } else {
            actualizeDatabase()
            Result.failure()
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName

        fun actualizeDatabase() {
            Log.d(TAG, "newTryActualizeDatabase: START Plan Next Work")

            val actualizeDatabaseWorkRequest = OneTimeWorkRequestBuilder<ActualizeDatabaseWorker>()
                .setInitialDelay(defaultDelay(), TimeUnit.MILLISECONDS)
                .build()

            Log.d(TAG, "newTryActualizeDatabase: enqueue Work!")

            WorkManager.getInstance(App())
                .beginUniqueWork(
                    "actualize_database_work",
                    ExistingWorkPolicy.REPLACE,
                    actualizeDatabaseWorkRequest
                )
                .enqueue()
        }
    }
}