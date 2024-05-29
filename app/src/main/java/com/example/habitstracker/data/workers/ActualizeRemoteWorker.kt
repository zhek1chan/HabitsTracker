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
import com.example.habitstracker.domain.usecase.GetNotActualHabitsUseCase
import com.example.habitstracker.domain.usecase.PutHabitToRemoteUseCase
import com.example.habitstracker.domain.usecase.UpdateHabitUseCase
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class ActualizeRemoteWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: START")
        var needRetry = false
        try {
            val repository = Repository(
                AppDataBase.INSTANCE!!,
                DoubletappApi(),
                HabitMapper(),
                HabitEntityMapper()
            )
            val notActualHabits = GetNotActualHabitsUseCase(repository, Dispatchers.IO)
                .getNotActualHabits()

            notActualHabits.forEach { habit ->
                try {
                    val entity = HabitMapper().map(habit)
                    PutHabitToRemoteUseCase(repository, Dispatchers.IO)
                        .putHabitToRemote(entity)?.let { uid ->
                            UpdateHabitUseCase(repository, Dispatchers.IO)
                                .updateHabit(entity.copy(uid = uid, isActual = true))
                        } ?: run { needRetry = true }
                } catch (e: Exception) {
                    Log.e(TAG, "ActualizeRemote ERROR!", e)
                    needRetry = true
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "ActualizeRemote ERROR!", e)
            needRetry = true
        }

        return if (!needRetry) {
            Result.success()
        } else {
            actualizeRemote()
            Result.failure()
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName

        fun actualizeRemote() {
            Log.d(TAG, "newTryActualizeRemote: START Plan Next Work")
            val actualizeRemoteWorkRequest = OneTimeWorkRequestBuilder<ActualizeRemoteWorker>()
                .setInitialDelay(defaultDelay(), TimeUnit.MILLISECONDS)
                .build()
            Log.d(TAG, "newTryActualizeRemote: enqueue Work!")

            WorkManager.getInstance(App())
                .beginUniqueWork(
                    "actualize_remote_work",
                    ExistingWorkPolicy.REPLACE,
                    actualizeRemoteWorkRequest
                )
                .enqueue()
        }
    }
}