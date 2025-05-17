package com.projects.hanoipetadoption.data.repository.postadoption

import com.projects.hanoipetadoption.data.mapper.toHealthRecord
import com.projects.hanoipetadoption.data.mapper.toReminder
import com.projects.hanoipetadoption.data.mapper.toReminderList
import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.data.source.postadoption.ReminderLocalDataSource
import com.projects.hanoipetadoption.data.source.postadoption.ReminderRemoteDataSource
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of ReminderRepository
 */
class ReminderRepositoryImpl(
    private val remoteDataSource: ReminderRemoteDataSource,
    private val localDataSource: ReminderLocalDataSource
) : ReminderRepository {    override suspend fun getUpcomingReminders(daysAhead: Int): Result<List<Reminder>> = 
        withContext(Dispatchers.IO) {
            return@withContext try {
                val healthRecords = remoteDataSource.getUpcomingReminders(daysAhead).map { it.toHealthRecord() }
                val reminders = healthRecords.toReminderList()
                localDataSource.saveReminders(healthRecords)
                Result.success(reminders)
            } catch (e: Exception) {
                    Result.failure(e)
            }
        }

    override suspend fun createVaccinationReminder(reminder: VaccinationReminderCreate): Result<Reminder> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val createdHealthRecord = remoteDataSource.createVaccinationReminder(reminder)
                val createdReminder = createdHealthRecord.toHealthRecord().toReminder()
                Result.success(createdReminder)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getRemindersForPet(petId: String): Result<List<Reminder>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val healthRecords = remoteDataSource.getRemindersForPet(petId).map { it.toHealthRecord() }
                val reminders = healthRecords.toReminderList()
                localDataSource.saveReminders(healthRecords)
                Result.success(reminders)
            } catch (e: Exception) {
                try {
                    val cachedReminders = localDataSource.getRemindersForPet(petId).map { it.toReminder() }
                    if (cachedReminders.isNotEmpty()) {
                        Result.success(cachedReminders)
                    } else {
                        Result.failure(e)
                    }
                } catch (cacheException: Exception) {
                    Result.failure(e)
                }
            }
        }

    override suspend fun markReminderComplete(reminderId: Int): Result<Boolean> = 
        withContext(Dispatchers.IO) {
            return@withContext try {
                val success = remoteDataSource.markReminderComplete(reminderId)
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteReminder(reminderId: Int): Result<Boolean> = 
        withContext(Dispatchers.IO) {
            return@withContext try {
                val success = remoteDataSource.deleteReminder(reminderId)
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
