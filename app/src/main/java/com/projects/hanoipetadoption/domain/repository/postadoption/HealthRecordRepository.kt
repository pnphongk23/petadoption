package com.projects.hanoipetadoption.domain.repository.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.data.util.Result
import java.io.File
import java.util.Date

/**
 * Repository interface for health record operations
 */
interface HealthRecordRepository {
    
    /**
     * Create a new health record
     * @param healthRecord The health record to create
     * @return Result containing the created health record or an error
     */
    suspend fun createHealthRecord(healthRecord: HealthRecord): Result<HealthRecord>
    
    /**
     * Get all health records for a pet
     * @param petId The ID of the pet
     * @param recordType Optional filter for record type
     * @param startDate Optional filter for start date
     * @param endDate Optional filter for end date
     * @return Result containing the list of health records or an error
     */
    suspend fun getHealthRecordsForPet(
        petId: String,
        recordType: RecordType? = null,
        startDate: Date? = null,
        endDate: Date? = null
    ): Result<List<HealthRecord>>
    
    /**
     * Get a specific health record by ID
     * @param recordId The ID of the health record
     * @return Result containing the health record or an error
     */
    suspend fun getHealthRecord(recordId: Int): Result<HealthRecord>
    
    /**
     * Update an existing health record
     * @param recordId The ID of the health record to update
     * @param healthRecord The updated health record
     * @return Result containing the updated health record or an error
     */
    suspend fun updateHealthRecord(recordId: Int, healthRecord: HealthRecord): Result<HealthRecord>
    
    /**
     * Delete a health record
     * @param recordId The ID of the health record to delete
     * @return Result indicating success or an error
     */
    suspend fun deleteHealthRecord(recordId: Int): Result<Unit>
    
    /**
     * Add media to a health record
     * @param recordId The ID of the health record
     * @param mediaFile The media file to upload
     * @param mediaType The type of media (image, video, document)
     * @return Result containing the created media item or an error
     */
    suspend fun addMediaToHealthRecord(
        recordId: Int,
        mediaFile: File,
        mediaType: String
    ): Result<HealthRecordMedia>
    
    /**
     * Get all media for a health record
     * @param recordId The ID of the health record
     * @return Result containing the list of media items or an error
     */
    suspend fun getMediaForHealthRecord(recordId: Int): Result<List<HealthRecordMedia>>
    
    /**
     * Delete media from a health record
     * @param recordId The ID of the health record
     * @param mediaId The ID of the media to delete
     * @return Result indicating success or an error
     */
    suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int): Result<Unit>
    
    /**
     * Get upcoming vaccination reminders
     * @param daysAhead Number of days to look ahead
     * @return Result containing the list of upcoming reminders or an error
     */
    suspend fun getUpcomingReminders(daysAhead: Int = 7): Result<List<HealthRecord>>
    
    /**
     * Create a vaccination reminder
     * @param petId The ID of the pet
     * @param name The name of the vaccination
     * @param notes Additional notes
     * @param reminderDate The date of the reminder
     * @param isRecurring Whether the reminder is recurring
     * @param recurrenceIntervalDays The interval for recurrence in days
     * @return Result containing the created reminder or an error
     */
    suspend fun createVaccinationReminder(
        petId: String,
        name: String,
        notes: String?,
        reminderDate: Date,
        isRecurring: Boolean = false,
        recurrenceIntervalDays: Int? = null
    ): Result<HealthRecord>

    /**
     * Clear outdated health records
     * @param olderThan Timestamp in milliseconds; records older than this will be cleared
     * @return Result indicating success or an error
     */
    suspend fun clearOutdatedHealthRecords(olderThan: Long): Result<Unit>
}
