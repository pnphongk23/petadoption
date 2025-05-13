package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordResponse
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMediaResponse
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordUpdate
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import java.io.File
import java.util.Date

/**
 * Interface for remote data source operations related to health records
 */
interface HealthRecordRemoteDataSource {
    
    /**
     * Create a new health record
     * @param record The record data to create
     * @return The created health record response
     */
    suspend fun createHealthRecord(record: HealthRecordCreate): HealthRecordResponse
    
    /**
     * Get health records for a pet
     * @param petId The ID of the pet
     * @param recordType Optional filter for record type
     * @param startDate Optional filter for start date
     * @param endDate Optional filter for end date
     * @return List of health record responses
     */
    suspend fun getHealthRecordsForPet(
        petId: Int,
        recordType: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): List<HealthRecordResponse>
    
    /**
     * Get a health record by ID
     * @param recordId The ID of the health record
     * @return The health record response
     */
    suspend fun getHealthRecord(recordId: Int): HealthRecordResponse
    
    /**
     * Update a health record
     * @param recordId The ID of the health record
     * @param update The update data
     * @return The updated health record response
     */
    suspend fun updateHealthRecord(
        recordId: Int,
        update: HealthRecordUpdate
    ): HealthRecordResponse
    
    /**
     * Delete a health record
     * @param recordId The ID of the health record to delete
     */
    suspend fun deleteHealthRecord(recordId: Int)
    
    /**
     * Add media to a health record
     * @param recordId The ID of the health record
     * @param file The media file
     * @param mediaType The type of media
     * @return The created media response
     */
    suspend fun addMediaToHealthRecord(
        recordId: Int,
        file: File,
        mediaType: String
    ): HealthRecordMediaResponse
    
    /**
     * Get media for a health record
     * @param recordId The ID of the health record
     * @return List of media responses
     */
    suspend fun getMediaForHealthRecord(recordId: Int): List<HealthRecordMediaResponse>
    
    /**
     * Delete media from a health record
     * @param recordId The ID of the health record
     * @param mediaId The ID of the media
     */
    suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int)
    
    /**
     * Get upcoming reminders
     * @param daysAhead Number of days to look ahead
     * @return List of upcoming reminders
     */
    suspend fun getUpcomingReminders(daysAhead: Int = 7): List<HealthRecordResponse>
    
    /**
     * Create a vaccination reminder
     * @param reminder The reminder data
     * @return The created reminder response
     */
    suspend fun createVaccinationReminder(reminder: VaccinationReminderCreate): HealthRecordResponse
}
