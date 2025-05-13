package com.projects.hanoipetadoption.data.source.postadoption

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.projects.hanoipetadoption.worker.ReminderNotificationWorker
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Implementation of ReminderLocalDataSource that uses Room database and WorkManager
 */
class ReminderLocalDataSourceImpl(
    private val context: Context,
    private val workManager: WorkManager
) : ReminderLocalDataSource {
    
    // In-memory cache for reminders (temporary until Room implementation)
    private val reminderCache = mutableMapOf<Int, HealthRecord>()
    
    override suspend fun saveReminders(reminders: List<HealthRecord>) {
        // Add to in-memory cache (temporary)
        reminders.forEach { reminder ->
            reminder.id?.let { id ->
                reminderCache[id] = reminder
                // Also schedule notifications for the reminder
                if (reminder.nextReminderDate != null) {
                    scheduleReminderNotification(reminder)
                }
            }
        }
        
        // TODO: Implement Room database storage
        // Example:
        // reminderDao.insertAll(reminders.map { it.toReminderEntity() })
    }
    
    override suspend fun getRemindersForPet(
        petId: Int,
        status: String?
    ): List<HealthRecord> {
        // Filter from in-memory cache (temporary)
        return reminderCache.values
            .filter { it.petId == petId }
            .filter {
                status == null || 
                (status == "completed" && it.nextReminderDate == null) ||
                (status == "scheduled" && it.nextReminderDate != null)
            }
            .sortedBy { it.nextReminderDate ?: Date(Long.MAX_VALUE) }
        
        // TODO: Implement Room database query
        // Example:
        // return reminderDao.getRemindersForPet(petId, status)
        //     .map { it.toHealthRecord() }
    }
    
    override suspend fun getReminder(reminderId: Int): HealthRecord? {
        // Return from in-memory cache (temporary)
        return reminderCache[reminderId]
        
        // TODO: Implement Room database query
        // Example:
        // val entity = reminderDao.getReminder(reminderId)
        // return entity?.toHealthRecord()
    }
    
    override suspend fun saveReminder(reminder: HealthRecord) {
        // Add to in-memory cache (temporary)
        reminder.id?.let { id ->
            reminderCache[id] = reminder
            
            // Schedule notification if it has a reminder date
            if (reminder.nextReminderDate != null) {
                scheduleReminderNotification(reminder)
            } else {
                cancelReminderNotification(id)
            }
        }
        
        // TODO: Implement Room database insertion
        // Example:
        // reminderDao.insert(reminder.toReminderEntity())
    }
    
    override suspend fun markReminderComplete(reminderId: Int) {
        // Update in-memory cache (temporary)
        reminderCache[reminderId]?.let { reminder ->
            // Mark as complete by removing the reminder date
            reminderCache[reminderId] = reminder.copy(nextReminderDate = null)
            
            // Cancel any scheduled notification
            cancelReminderNotification(reminderId)
        }
        
        // TODO: Implement Room database update
        // Example:
        // reminderDao.markComplete(reminderId)
    }
    
    override suspend fun updateReminder(
        reminderId: Int,
        reminderDate: Date,
        notes: String?
    ) {
        // Update in-memory cache (temporary)
        reminderCache[reminderId]?.let { reminder ->
            val updatedReminder = reminder.copy(
                nextReminderDate = reminderDate,
                notes = notes ?: reminder.notes
            )
            reminderCache[reminderId] = updatedReminder
            
            // Update the notification
            scheduleReminderNotification(updatedReminder)
        }
        
        // TODO: Implement Room database update
        // Example:
        // reminderDao.updateReminder(reminderId, reminderDate, notes)
    }
    
    override suspend fun deleteReminder(reminderId: Int) {
        // Remove from in-memory cache (temporary)
        reminderCache.remove(reminderId)
        
        // Cancel any scheduled notification
        cancelReminderNotification(reminderId)
        
        // TODO: Implement Room database deletion
        // Example:
        // reminderDao.deleteReminder(reminderId)
    }
    
    override suspend fun getUpcomingReminders(daysAhead: Int): List<HealthRecord> {
        val cutoffTime = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(daysAhead.toLong()))
        
        // Filter from in-memory cache (temporary)
        return reminderCache.values
            .filter { it.nextReminderDate != null && it.nextReminderDate.before(cutoffTime) }
            .sortedBy { it.nextReminderDate }
        
        // TODO: Implement Room database query
        // Example:
        // val cutoffDate = SimpleDateFormat("yyyy-MM-dd").format(cutoffTime)
        // return reminderDao.getUpcomingReminders(cutoffDate)
        //     .map { it.toHealthRecord() }
    }
    
    override suspend fun scheduleReminderNotification(reminder: HealthRecord) {
        reminder.id?.let { id ->
            val reminderDate = reminder.nextReminderDate ?: return
            
            // Calculate delay until reminder time
            val currentTime = System.currentTimeMillis()
            val reminderTime = reminderDate.time
            
            // Only schedule if the reminder is in the future
            if (reminderTime > currentTime) {
                val delay = reminderTime - currentTime
                  // Create work data
                val inputData = workDataOf(
                    "reminderId" to id,
                    "petId" to reminder.petId,
                    "title" to "Pet Vaccination Reminder",
                    "content" to (reminder.notes ?: "Time for your pet's vaccination")
                )
                
                // Create work request
                val notificationWork = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
                    .setInputData(inputData)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .addTag("reminder_$id")
                    .build()
                
                // Schedule unique work
                workManager.enqueueUniqueWork(
                    "reminder_$id",
                    ExistingWorkPolicy.REPLACE,
                    notificationWork
                )
            }
        }
    }
    
    override suspend fun cancelReminderNotification(reminderId: Int) {
        // Cancel any scheduled notification work
        workManager.cancelUniqueWork("reminder_$reminderId")
    }    // ReminderNotificationWorker is now in a separate file
}
