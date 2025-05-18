package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import java.util.Date
import kotlinx.coroutines.flow.Flow

/**
 * Interface for local data source operations related to reminders
 */
interface ReminderLocalDataSource {
    
    /**
     * Save reminders to local storage
     * @param reminders List of reminders to save
     */
    suspend fun saveReminders(reminders: List<HealthRecord>)
    
    /**
     * Get reminders for a pet from local storage
     * @param petId The ID of the pet
     * @param status Optional filter for reminder status
     * @return Flow emitting a list of reminder records
     */
    fun getRemindersForPet(
        petId: String,
        status: String? = null
    ): Flow<List<HealthRecord>>
    
    /**
     * Get a reminder by ID from local storage
     * @param reminderId The ID of the reminder
     * @return The reminder record or null if not found
     */
    suspend fun getReminder(reminderId: Int): HealthRecord?
    
    /**
     * Save a reminder to local storage
     * @param reminder The reminder to save
     * @return The saved HealthRecord, potentially with an updated ID from the database
     */
    suspend fun saveReminder(reminder: HealthRecord): HealthRecord
    
    /**
     * Mark a reminder as complete in local storage
     * @param reminderId The ID of the reminder
     */
    suspend fun markReminderComplete(reminderId: Int)
    
    /**
     * Update a reminder in local storage
     * @param reminderId The ID of the reminder
     * @param reminderDate New reminder date
     * @param notes Updated notes
     */
    suspend fun updateReminder(
        reminderId: Int,
        reminderDate: Date,
        notes: String? = null
    )
    
    /**
     * Delete a reminder from local storage
     * @param reminderId The ID of the reminder to delete
     */
    suspend fun deleteReminder(reminderId: Int)
    
    /**
     * Get upcoming reminders from local storage
     * @param daysAhead Number of days to look ahead
     * @return Flow emitting a list of upcoming reminders
     */
    fun getUpcomingReminders(daysAhead: Int = 7): Flow<List<HealthRecord>>
    
    /**
     * Schedule a notification for a reminder
     * @param reminder The reminder to schedule a notification for
     */
    suspend fun scheduleReminderNotification(reminder: HealthRecord)
    
    /**
     * Cancel a scheduled notification for a reminder
     * @param reminderId The ID of the reminder
     */
    suspend fun cancelReminderNotification(reminderId: Int)
}
