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
import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.data.network.DoubletappApi
import com.example.habitstracker.data.network.HabitEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class ActualizeDatabaseWorker(private val ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: START")

        return if (updateHabitsFromRemote(ctx)) {
            Result.success()
        } else {
            actualizeDatabase()
            Result.failure()
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
        private val db = AppDataBase
        suspend fun updateHabitsFromRemote(context: Context): Boolean {
            var isUpdated = false

            try {
                val habitResponse = DoubletappApi.habitApiService.getHabits()

                if (habitResponse.isSuccessful) {
                    val habits = habitResponse.body()
                    val allHabits = db(context).habitDao().getAll().value

                    habits?.let { remoteHabits ->
                        var list: MutableList<HabitEntity> = mutableListOf()
                        remoteHabits.forEach {
                            list.add(HabitEntityMapper().map(it))
                        }
                        val actualRemoteHabits = getMergedActualHabits(list, allHabits)
                        insertAllHabits(actualRemoteHabits, context)
                        isUpdated = true
                    }
                } else {
                    habitResponse.errorBody()?.let {
                        throw Exception("Ошибка! Код: ${habitResponse.code()}, Текст: $it, $habitResponse")
                    } ?: kotlin.run {
                        throw Exception("Ошибка! Код: ${habitResponse.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "updateHabitsFromRemote: ", e)
            }

            return isUpdated
        }

        private fun getMergedActualHabits(
            remoteHabits: List<HabitEntity>,
            databaseHabits: List<HabitEntity>?
        ): List<HabitEntity> {

            if (remoteHabits.isEmpty()) {
                return emptyList()
            }

            val remoteHabitsMap = remoteHabits.associateBy { it.uid } as HashMap

            databaseHabits?.forEach { databaseHabit ->
                if (!databaseHabit.isActual) {
                    remoteHabitsMap.remove(databaseHabit.uid)
                } else if (databaseHabit.uid.isNotBlank() && databaseHabit.isActual) {
                    if (remoteHabitsMap.containsKey(databaseHabit.uid)) {
                        val habitWithId =
                            remoteHabitsMap[databaseHabit.uid]!!.copy(id = databaseHabit.id)
                        remoteHabitsMap[databaseHabit.uid] = habitWithId
                    }
                }
            }
            return remoteHabitsMap.values.map { it.copy(isActual = true) }
        }

        private suspend fun insertAllHabits(habits: List<HabitEntity>, context: Context) =
            withContext(Dispatchers.IO) {
                if (habits.isNotEmpty()) {
                    db(context).habitDao().deleteSame()
                    habits.forEach {
                        db(context).habitDao().insert(
                            HabitEntity(
                                null,
                                it.uid,
                                it.title,
                                it.description,
                                it.type,
                                it.priority,
                                it.color,
                                it.frequency,
                                it.count,
                                it.lastModified,
                                it.date,
                                it.isActual
                            )
                        )
                    }
                    Log.d("INSERTED FROM SERV", "$habits")
                }
            }

        fun actualizeDatabase() {
            Log.d(TAG, "newTryActualizeDatabase: START Plan Next Work")

            val actualizeDatabaseWorkRequest = OneTimeWorkRequestBuilder<ActualizeDatabaseWorker>()
                .setInitialDelay(defaultDelay(), TimeUnit.MILLISECONDS)
                .build()

            Log.d(TAG, "newTryActualizeDatabase: enqueue Work!")

            WorkManager.getInstance(App())
                .beginUniqueWork(
                    "actualizing_db",
                    ExistingWorkPolicy.REPLACE,
                    actualizeDatabaseWorkRequest
                )
                .enqueue()
        }
    }
}