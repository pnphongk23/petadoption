package com.projects.hanoipetadoption.data.source.postadoption

import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.projects.hanoipetadoption.data.local.dao.ReminderDao
import com.projects.hanoipetadoption.data.mapper.toHealthRecord
import com.projects.hanoipetadoption.data.mapper.toHealthRecordList
import com.projects.hanoipetadoption.data.mapper.toReminderEntity
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.worker.ReminderNotificationWorker
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of ReminderLocalDataSource that uses Room database and WorkManager
 */
class ReminderLocalDataSourceImpl(
    private val reminderDao: ReminderDao,
    private val workManager: WorkManager
) : ReminderLocalDataSource {
    
    override suspend fun saveReminders(reminders: List<HealthRecord>) {
        reminders.forEach { healthRecord ->
            // Save each record and get its generated ID for notification scheduling
            val entity = healthRecord.toReminderEntity()
            val generatedId = reminderDao.insertReminder(entity) // Returns Long

            val recordForNotification = healthRecord.copy(id = generatedId.toInt())
            if (recordForNotification.nextReminderDate != null && recordForNotification.id != null && recordForNotification.id != 0) {
                scheduleReminderNotification(recordForNotification)
            }
        }
    }
    
    override fun getRemindersForPet(
        petId: String,
        status: String?
    ): Flow<List<HealthRecord>> {
        return reminderDao.getRemindersForPet(petId) 
            .map { entities -> 
                val healthRecords = entities.toHealthRecordList()
                // Apply status filtering based on the properties of HealthRecord
                healthRecords
                    .filter { hr ->
                        status == null ||
                                (status.equals("completed", ignoreCase = true) && hr.nextReminderDate == null) || 
                                (status.equals("scheduled", ignoreCase = true) && hr.nextReminderDate != null)
                    }
                    .sortedBy { it.nextReminderDate ?: Date(Long.MAX_VALUE) }
            }
    }
    
    override suspend fun getReminder(reminderId: Int): HealthRecord? {
        val entity = reminderDao.getReminderById(reminderId.toLong())
        return entity?.toHealthRecord()
    }
    
    override suspend fun saveReminder(reminder: HealthRecord): HealthRecord {
        val entity = reminder.toReminderEntity() // id might be 0L if new
        val generatedId = reminderDao.insertReminder(entity) // Returns new rowId as Long

        val savedHealthRecord = reminder.copy(id = generatedId.toInt())

        // Schedule notification with the HealthRecord that has the correct ID
        if (savedHealthRecord.nextReminderDate != null && savedHealthRecord.id != 0) {
            scheduleReminderNotification(savedHealthRecord)
        } else if (savedHealthRecord.id != 0) { 
            cancelReminderNotification(savedHealthRecord.id!!) 
        }
        return savedHealthRecord // Return the HealthRecord with the DB-generated ID
    }
    
    override suspend fun markReminderComplete(reminderId: Int) {
        reminderDao.markReminderAsComplete(reminderId.toLong())
        cancelReminderNotification(reminderId) // Cancel any scheduled notification for this reminder
    }
    
    override suspend fun updateReminder(
        reminderId: Int,
        reminderDate: Date,
        notes: String?
    ) {
        val existingEntity = reminderDao.getReminderById(reminderId.toLong())
        existingEntity?.let {
            // When updating, it's no longer completed if it was
            val updatedEntity = it.copy(
                dueDate = reminderDate,
                description = notes ?: it.description, // Keep old notes if new one is null
                isCompleted = false // An updated reminder is implicitly not completed
            )
            reminderDao.updateReminder(updatedEntity)

            // Reschedule notification with updated info
            val healthRecordForNotification = updatedEntity.toHealthRecord()
            scheduleReminderNotification(healthRecordForNotification)
        }
    }
    
    override suspend fun deleteReminder(reminderId: Int) {
        reminderDao.deleteReminderById(reminderId.toLong())
        cancelReminderNotification(reminderId) // Cancel any scheduled notification
    }
    
    override fun getUpcomingReminders(daysAhead: Int): Flow<List<HealthRecord>> {
        val currentTime = System.currentTimeMillis()
        val cutoffTime = currentTime + TimeUnit.DAYS.toMillis(daysAhead.toLong())

        // DAO method getUpcomingReminders already filters by is_completed = 0 and sorts by due_date
        return reminderDao.getUpcomingReminders(currentTime, cutoffTime)
            .map { entities -> 
                entities.toHealthRecordList()
                // The DAO query already sorts and filters by is_completed = 0.
                // Additional HealthRecord specific filtering/sorting could go here if needed.
            }
    }
    
    override suspend fun scheduleReminderNotification(reminder: HealthRecord) {
        val reminderIdInt = reminder.id ?: return
        if (reminderIdInt == 0) return

        val reminderDate = reminder.nextReminderDate ?: return

        val currentTime = System.currentTimeMillis()
        val reminderTime = reminderDate.time

        if (reminderTime > currentTime) {
            val delay = reminderTime - currentTime

            val notificationTitle = reminder.notes?.lineSequence()?.firstOrNull()?.take(100) ?: "Pet Reminder"
            val notificationContent = reminder.notes ?: "Check your pet's upcoming event."

            val inputData = workDataOf(
                ReminderNotificationWorker.KEY_REMINDER_ID to reminderIdInt,
                ReminderNotificationWorker.KEY_PET_ID to reminder.petId, // Assuming HealthRecord.petId is String, but Worker expects Int. This needs to be checked.
                ReminderNotificationWorker.KEY_TITLE to notificationTitle,
                ReminderNotificationWorker.KEY_CONTENT to notificationContent
            )

            val notificationWorkRequest = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
                .setInputData(inputData)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag("reminder_tag_${reminderIdInt}")
                .build()

            workManager.enqueueUniqueWork(
                "reminder_work_${reminderIdInt}",
                ExistingWorkPolicy.REPLACE,
                notificationWorkRequest
            )
        }
    }
    
    override suspend fun cancelReminderNotification(reminderId: Int) {
        if (reminderId == 0) return
        workManager.cancelUniqueWork("reminder_work_${reminderId}")
        workManager.cancelAllWorkByTag("reminder_tag_${reminderId}") // Also cancel by tag for safety
    }
}
