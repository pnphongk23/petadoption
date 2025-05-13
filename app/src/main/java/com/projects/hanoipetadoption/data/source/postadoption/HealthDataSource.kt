package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordUpdate
import com.projects.hanoipetadoption.data.model.postadoption.MediaType
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import java.io.File
import java.util.Date

/**
 * Interface for health record remote data source
 */
interface HealthRemoteDataSource {
    /**
     * Get health records for a specific pet
     */
    suspend fun getHealthRecordsForPet(
        petId: Int,
        recordType: RecordType? = null,
        startDate: Date? = null,
        endDate: Date? = null
    ): List<HealthRecord>

    /**
     * Get a health record by ID
     */
    suspend fun getHealthRecordById(recordId: Int): HealthRecord?

    /**
     * Create a health record
     */
    suspend fun createHealthRecord(healthRecord: HealthRecordCreate): HealthRecord

    /**
     * Update a health record
     */
    suspend fun updateHealthRecord(recordId: Int, update: HealthRecordUpdate): HealthRecord

    /**
     * Delete a health record
     */
    suspend fun deleteHealthRecord(recordId: Int): Boolean

    /**
     * Add media to a health record
     */
    suspend fun addMediaToHealthRecord(
        recordId: Int,
        file: File,
        mediaType: MediaType
    ): HealthRecordMedia

    /**
     * Get media for a health record
     */
    suspend fun getMediaForHealthRecord(recordId: Int): List<HealthRecordMedia>

    /**
     * Delete media from a health record
     */
    suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int): Boolean
}