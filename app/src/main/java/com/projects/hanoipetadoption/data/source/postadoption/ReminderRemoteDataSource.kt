package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordResponse
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import java.util.Date

/**
 * Interface for remote data source operations related to reminders
 */
interface ReminderRemoteDataSource {
    
    /**
     * Get reminders for a pet
     * @param petId The ID of the pet
     * @param status Optional filter for reminder status (scheduled, completed, etc.)
     * @param startDate Optional filter for start date
     * @param endDate Optional filter for end date
     * @return List of health record responses that are reminders
     */
    suspend fun getRemindersForPet(
        petId: Int,
        status: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): List<HealthRecordResponse>
    
    /**
     * Create a vaccination reminder
     * @param reminder The reminder data
     * @return The created reminder response
     */
    suspend fun createVaccinationReminder(
        reminder: VaccinationReminderCreate
    ): HealthRecordResponse
    
    /**
     * Mark a reminder as complete
     * @param reminderId The ID of the reminder
     * @return The updated reminder response
     */
    suspend fun markReminderComplete(reminderId: Int): HealthRecordResponse
    
    /**
     * Get upcoming reminders
     * @param daysAhead Number of days to look ahead
     * @return List of upcoming reminder responses
     */
    suspend fun getUpcomingReminders(daysAhead: Int = 7): List<HealthRecordResponse>
    
    /**
     * Update a reminder
     * @param reminderId The ID of the reminder
     * @param reminderDate New reminder date
     * @param notes Updated notes
     * @return The updated reminder response
     */
    suspend fun updateReminder(
        reminderId: Int,
        reminderDate: String,
        notes: String? = null
    ): HealthRecordResponse
    
    /**
     * Delete a reminder
     * @param reminderId The ID of the reminder to delete
     */
    suspend fun deleteReminder(reminderId: Int)
}
