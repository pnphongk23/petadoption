package com.projects.hanoipetadoption.data.repository.postadoption

import com.projects.hanoipetadoption.data.mapper.toHealthRecord
import com.projects.hanoipetadoption.data.mapper.toReminder
import com.projects.hanoipetadoption.data.mapper.toReminderList
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.data.source.postadoption.ReminderLocalDataSource
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Implementation of ReminderRepository
 */
class ReminderRepositoryImpl(
    private val localDataSource: ReminderLocalDataSource
) : ReminderRepository {
    override fun getUpcomingReminders(daysAhead: Int): Flow<List<Reminder>> {
        return localDataSource.getUpcomingReminders(daysAhead)
            .map { healthRecords ->
                healthRecords.toReminderList()
            }
    }

    override suspend fun createVaccinationReminder(reminder: VaccinationReminderCreate): Result<Reminder> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val newHealthRecordFields = HealthRecord(
                    id = null, 
                    petId = reminder.petId,
                    userId = null, 
                    recordType = RecordType.VACCINATION,
                    notes = reminder.notes,
                    weight = null, 
                    recordDate = java.util.Date(),
                    nextReminderDate = reminder.reminderDate,
                    mediaItems = emptyList()
                )
                val savedHealthRecordWithId = localDataSource.saveReminder(newHealthRecordFields)
                val createdReminderDomainModel = savedHealthRecordWithId.toReminder()
                Result.success(createdReminderDomainModel)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override fun getRemindersForPet(petId: String): Flow<List<Reminder>> {
        return localDataSource.getRemindersForPet(petId)
            .map { healthRecords ->
                healthRecords.toReminderList()
            }
    }

    override suspend fun markReminderComplete(reminderId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                localDataSource.markReminderComplete(reminderId)
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteReminder(reminderId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                localDataSource.deleteReminder(reminderId)
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
