package com.projects.hanoipetadoption.domain.repository.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for reminders
 */
interface ReminderRepository {
    /**
     * Get upcoming reminders for the user's pets
     * 
     * @param daysAhead Number of days ahead to check
     * @return A Flow emitting a list of upcoming reminders
     */
    fun getUpcomingReminders(daysAhead: Int): Flow<List<Reminder>>

    /**
     * Create a vaccination reminder
     * 
     * @param reminder The vaccination reminder to create
     * @return The created reminder
     */
    suspend fun createVaccinationReminder(reminder: VaccinationReminderCreate): Result<Reminder>

    /**
     * Get reminders for a specific pet
     * 
     * @param petId ID of the pet
     * @return A Flow emitting a list of reminders for the pet
     */
    fun getRemindersForPet(petId: String): Flow<List<Reminder>>

    /**
     * Mark a reminder as completed
     * 
     * @param reminderId ID of the reminder to mark as completed
     * @return Whether the update was successful
     */
    suspend fun markReminderComplete(reminderId: Int): Result<Boolean>

    /**
     * Delete a reminder
     * 
     * @param reminderId ID of the reminder to delete
     * @return Whether the deletion was successful
     */
    suspend fun deleteReminder(reminderId: Int): Result<Boolean>
}
