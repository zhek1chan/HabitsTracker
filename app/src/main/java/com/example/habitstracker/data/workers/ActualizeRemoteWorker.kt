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
import com.example.habitstracker.data.network.DoubletappApi
import com.example.habitstracker.data.network.HabitEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class ActualizeRemoteWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    private val db = AppDataBase
    private val convertor = HabitEntityMapper()
    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: START")
        var needRetry = false

        try {
            val notActualHabits = withContext(Dispatchers.IO) {
                Log.d("CHET HUINYA", "KAKAYATO")
                db(context).habitDao().getNotActualHabits()
            }

            notActualHabits.forEach { habit ->
                try {
                    val response = DoubletappApi.habitApiService.putHabit(convertor.map(habit))

                    if (response.isSuccessful) {
                        val putHabitResponse = response.body()

                        putHabitResponse?.let {
                            db(context).habitDao()
                                .update(habit.copy(uid = putHabitResponse.uid, isActual = true))
                        }
                    } else {
                        response.errorBody()?.let {
                            throw Exception("Ошибка! Код: ${response.code()}, Текст: ${response.message()}, $response")
                        } ?: kotlin.run {
                            throw Exception("Ошибка! Код: ${response.code()}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "ActualizeRemote ERROR!", e)
                    needRetry = true
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "ActualizeRemote ERROR!", e)
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
                    "actualizing_remote",
                    ExistingWorkPolicy.REPLACE,
                    actualizeRemoteWorkRequest
                )
                .enqueue()
        }
    }
}