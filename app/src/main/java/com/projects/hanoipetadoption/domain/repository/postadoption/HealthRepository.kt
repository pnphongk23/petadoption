package com.projects.hanoipetadoption.domain.repository.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordUpdate
import com.projects.hanoipetadoption.data.model.postadoption.MediaType
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import java.io.File
import java.util.Date

/**
 * Repository interface for health records
 */
interface HealthRepository {
    /**
     * Get health records for a specific pet
     * 
     * @param petId ID of the pet
     * @param recordType Optional filter for record type
     * @param startDate Optional start date for filter
     * @param endDate Optional end date for filter
     * @return List of health records
     */
    suspend fun getHealthRecordsForPet(
        petId: String,
        recordType: RecordType? = null,
        startDate: Date? = null,
        endDate: Date? = null
    ): Result<List<HealthRecord>>

    /**
     * Get a specific health record by ID
     * 
     * @param recordId ID of the health record
     * @return The health record or null if not found
     */
    suspend fun getHealthRecordById(recordId: Int): Result<HealthRecord?>

    /**
     * Create a new health record
     * 
     * @param healthRecord The health record to create
     * @return The created health record
     */
    suspend fun createHealthRecord(healthRecord: HealthRecordCreate): Result<HealthRecord>

    /**
     * Update a health record
     * 
     * @param recordId ID of the health record to update
     * @param update The updated data
     * @return The updated health record
     */
    suspend fun updateHealthRecord(recordId: Int, update: HealthRecordUpdate): Result<HealthRecord>

    /**
     * Delete a health record
     * 
     * @param recordId ID of the health record to delete
     * @return Whether the deletion was successful
     */
    suspend fun deleteHealthRecord(recordId: Int): Result<Boolean>

    /**
     * Add media to a health record
     * 
     * @param recordId ID of the health record
     * @param file The file to upload
     * @param mediaType Type of the media
     * @return The created media record
     */
    suspend fun addMediaToHealthRecord(
        recordId: Int,
        file: File,
        mediaType: MediaType
    ): Result<HealthRecordMedia>

    /**
     * Get all media for a health record
     * 
     * @param recordId ID of the health record
     * @return List of media items
     */
    suspend fun getMediaForHealthRecord(recordId: Int): Result<List<HealthRecordMedia>>

    /**
     * Delete media from a health record
     * 
     * @param recordId ID of the health record
     * @param mediaId ID of the media to delete
     * @return Whether the deletion was successful
     */
    suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int): Result<Boolean>
}
