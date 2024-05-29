package com.example.habitstracker.data.repository

import android.util.Log
import com.example.habitstracker.data.db.AppDataBase
import com.example.habitstracker.data.db.HabitEntity
import com.example.habitstracker.data.db.HabitMapper
import com.example.habitstracker.data.db.HabitsDao
import com.example.habitstracker.data.network.DoubletappApi
import com.example.habitstracker.data.network.HabitEntityMapper
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.domain.repository.HabitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class Repository(
    database: AppDataBase,
    private val habitApi: DoubletappApi,
    private val mapper: HabitMapper,
    private val mapperEntityMapper: HabitEntityMapper
) : HabitsRepository {
    private val habitsDao: HabitsDao

    init {
        habitsDao = database.habitDao()
    }

    override fun getAllHabits(): Flow<List<Habit>> = flow {
        val habitsList = habitsDao.getAll()
        var habitsListMapped = mutableListOf<Habit>()
        habitsList.forEach {
            habitsListMapped.add(mapper.map(it))
        }
        emit(habitsListMapped)
    }

    override suspend fun getHabitById(id: Int): Habit {
        return mapper.map(habitsDao.getHabitById(id))

    }

    override suspend fun insertHabit(habit: Habit) = habitsDao.insert(mapper.map(habit))

    override suspend fun updateHabit(habit: HabitEntity) = habitsDao.update(habit)

    private fun getMergedActualHabits(
        remoteHabits: List<HabitEntity>,
        databaseHabits: List<HabitEntity>
    ): List<HabitEntity> {

        if (remoteHabits.isEmpty()) {
            return emptyList()
        }

        val remoteHabitsMap = remoteHabits.associateBy { it.uid } as HashMap

        databaseHabits.forEach { databaseHabit ->
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

    override suspend fun updateHabitsFromRemote(): Boolean {
        var isUpdated = false

        try {
            val habitResponse = habitApi.habitApiService.getHabits()

            if (habitResponse.isSuccessful) {
                val habits = habitResponse.body()
                val allHabits = habitsDao.getAll()

                habits?.let { remoteHabits ->
                    var list: MutableList<HabitEntity> = mutableListOf()
                    remoteHabits.forEach {
                        list.add(HabitEntityMapper().map(it))
                    }
                    val actualRemoteHabits = getMergedActualHabits(list, allHabits)
                    insertAllHabits(actualRemoteHabits)
                    isUpdated = true
                }
            } else {
                habitResponse.errorBody()?.let {
                    throw Exception("Ошибка! Код: ${habitResponse.code()}, Текст: $it")
                } ?: kotlin.run {
                    throw Exception("Ошибка! Код: ${habitResponse.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateHabitsFromRemote: ", e)
        }

        return isUpdated

    }

    private suspend fun insertAllHabits(habits: List<HabitEntity>) =
        withContext(Dispatchers.IO) {
            if (habits.isNotEmpty()) {
                habitsDao.deleteSame()
                habits.forEach {
                    habitsDao.insert(
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

    override suspend fun putHabitToRemote(habit: HabitEntity): String? {
        val response = habitApi.habitApiService.putHabit(mapperEntityMapper.map(habit))

        return if (response.isSuccessful) {
            val putHabitResponse = response.body()

            putHabitResponse?.let {
                putHabitResponse.uid
            }
        } else {
            null
        }
    }

    override suspend fun getNotActualHabits(): List<Habit> {
        TODO("Not yet implemented")
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}