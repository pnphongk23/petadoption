package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordResponse
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.data.network.PostAdoptionApiService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Implementation of ReminderRemoteDataSource that uses PostAdoptionApiService
 */
class ReminderRemoteDataSourceImpl(
    private val apiService: PostAdoptionApiService
) : ReminderRemoteDataSource {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    
    override suspend fun getRemindersForPet(
        petId: String,
        status: String?,
        startDate: String?,
        endDate: String?
    ): List<HealthRecordResponse> {
        // Use the health records endpoint but filter for reminder types
        // We'll assume all health records with a nextReminderDate are reminders
        val recordType = "VACCINATION" // Only fetch vaccination records as reminders for now
        
        return apiService.getHealthRecordsForPet(
            petId = petId,
            recordType = recordType,
            startDate = startDate,
            endDate = endDate
        ).filter { it.nextReminderDate != null } // Extra filter for reminders
    }
    
    override suspend fun createVaccinationReminder(
        reminder: VaccinationReminderCreate
    ): HealthRecordResponse {
        return apiService.createVaccinationReminder(reminder)
    }
    
    override suspend fun markReminderComplete(reminderId: Int): HealthRecordResponse {
        // For now, marking a reminder as complete just removes the reminder date
        // This assumes the API has this functionality
        val update = mapOf(
            "next_reminder_date" to null,
            "status" to "completed"
        )
        
        // This is a simplification - the actual implementation would depend on the API
        // We're assuming a PATCH endpoint exists for partial updates
        return apiService.updateHealthRecord(reminderId, update.toDomainUpdate())
    }
    
    override suspend fun getUpcomingReminders(daysAhead: Int): List<HealthRecordResponse> {
        return apiService.getUpcomingReminders(daysAhead)
    }
    
    override suspend fun updateReminder(
        reminderId: Int,
        reminderDate: String,
        notes: String?
    ): HealthRecordResponse {
        val update = mapOf(
            "next_reminder_date" to reminderDate,
            "notes" to notes
        )
        
        return apiService.updateHealthRecord(reminderId, update.toDomainUpdate())
    }
    
    override suspend fun deleteReminder(reminderId: Int) {
        apiService.deleteHealthRecord(reminderId)
    }
    
    /**
     * Helper function to format a date for API calls
     */
    private fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
    
    /**
     * Helper extension function to convert a map to a domain update object
     * This is a temporary solution until proper models are created
     */
    private fun Map<String, Any?>.toDomainUpdate(): com.projects.hanoipetadoption.data.model.postadoption.HealthRecordUpdate {
        // This is a simplification - the actual implementation would depend on the domain model
        return com.projects.hanoipetadoption.data.model.postadoption.HealthRecordUpdate(
            recordType = com.projects.hanoipetadoption.data.model.postadoption.RecordType.VACCINATION,
            notes = this["notes"] as? String,
            weight = null,
            nextReminderDate = (this["next_reminder_date"] as? String)?.let { dateStr ->
                try {
                    dateFormat.parse(dateStr)
                } catch (e: Exception) {
                    null
                }
            }
        )
    }
}
