package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia

/**
 * Interface for local data source operations related to health records
 */
interface HealthLocalDataSource {
    
    /**
     * Save health records to local storage
     * @param records List of health records to save
     */
    suspend fun saveHealthRecords(records: List<HealthRecord>)
    
    /**
     * Get health records for a pet from local storage
     * @param petId The ID of the pet
     * @param recordType Optional filter for record type
     * @return List of health records
     */
    suspend fun getHealthRecordsForPet(
        petId: String,
        recordType: String? = null
    ): List<HealthRecord>
    
    /**
     * Get a health record by ID from local storage
     * @param recordId The ID of the health record
     * @return The health record or null if not found
     */
    suspend fun getHealthRecord(recordId: Int): HealthRecord?
    
    /**
     * Save a health record to local storage
     * @param record The health record to save
     */
    suspend fun saveHealthRecord(record: HealthRecord)
    
    /**
     * Delete a health record from local storage
     * @param recordId The ID of the health record to delete
     */
    suspend fun deleteHealthRecord(recordId: Int)
    
    /**
     * Save media items for a health record to local storage
     * @param recordId The ID of the health record
     * @param media List of media items to save
     */
    suspend fun saveMediaForHealthRecord(recordId: Int, media: List<HealthRecordMedia>)
    
    /**
     * Get media items for a health record from local storage
     * @param recordId The ID of the health record
     * @return List of media items
     */
    suspend fun getMediaForHealthRecord(recordId: Int): List<HealthRecordMedia>
    
    /**
     * Delete media item from local storage
     * @param recordId The ID of the health record
     * @param mediaId The ID of the media item to delete
     */
    suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int)
    
    /**
     * Get upcoming reminders from local storage
     * @param daysAhead Number of days to look ahead
     * @return List of health records that are reminders
     */
    suspend fun getUpcomingReminders(daysAhead: Int = 7): List<HealthRecord>
    
    /**
     * Clear outdated records
     * @param olderThan Timestamp in milliseconds; records older than this will be cleared
     */
    suspend fun clearOutdatedRecords(olderThan: Long)
}
