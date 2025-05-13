package com.projects.hanoipetadoption.data.source.postadoption

import android.content.Context
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import java.io.File
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Implementation of HealthLocalDataSource that uses Room database and file system
 */
class HealthLocalDataSourceImpl(
    private val context: Context
) : HealthLocalDataSource {
    
    // Cache directory for health record media files
    private val healthRecordMediaDir: File by lazy {
        File(context.filesDir, "health_records").apply {
            if (!exists()) mkdirs()
        }
    }
    
    // In-memory cache for health records (temporary until Room implementation)
    private val healthRecordsCache = mutableMapOf<Int, HealthRecord>()
    private val mediaCache = mutableMapOf<Int, MutableList<HealthRecordMedia>>()
    
    override suspend fun saveHealthRecords(records: List<HealthRecord>) {
        // Add to in-memory cache (temporary)
        records.forEach { record ->
            record.id?.let { id ->
                healthRecordsCache[id] = record
                record.mediaItems.forEach { media ->
                    if (!mediaCache.containsKey(id)) {
                        mediaCache[id] = mutableListOf()
                    }
                    mediaCache[id]?.add(media)
                }
            }
        }
        
        // TODO: Implement Room database storage
        // Example:
        // healthRecordDao.insertAll(records.map { it.toHealthRecordEntity() })
    }
    
    override suspend fun getHealthRecordsForPet(
        petId: Int,
        recordType: String?
    ): List<HealthRecord> {
        // Return from in-memory cache (temporary)
        return healthRecordsCache.values
            .filter { it.petId == petId }
            .filter { recordType == null || it.recordType.name == recordType }
        
        // TODO: Implement Room database query
        // Example:
        // return healthRecordDao.getHealthRecordsForPet(petId, recordType)
        //     .map { it.toHealthRecord() }
    }
    
    override suspend fun getHealthRecord(recordId: Int): HealthRecord? {
        // Return from in-memory cache (temporary)
        return healthRecordsCache[recordId]
        
        // TODO: Implement Room database query
        // Example:
        // val entity = healthRecordDao.getHealthRecord(recordId)
        // return entity?.toHealthRecord()
    }
    
    override suspend fun saveHealthRecord(record: HealthRecord) {
        // Add to in-memory cache (temporary)
        record.id?.let { id ->
            healthRecordsCache[id] = record
            
            // Also update media cache
            mediaCache[id] = record.mediaItems.toMutableList()
        }
        
        // TODO: Implement Room database insertion
        // Example:
        // healthRecordDao.insert(record.toHealthRecordEntity())
    }
    
    override suspend fun deleteHealthRecord(recordId: Int) {
        // Remove from in-memory cache (temporary)
        healthRecordsCache.remove(recordId)
        mediaCache.remove(recordId)
        
        // Also delete media files
        val mediaDir = File(healthRecordMediaDir, recordId.toString())
        if (mediaDir.exists()) {
            mediaDir.deleteRecursively()
        }
        
        // TODO: Implement Room database deletion
        // Example:
        // healthRecordDao.deleteHealthRecord(recordId)
    }
    
    override suspend fun saveMediaForHealthRecord(
        recordId: Int,
        media: List<HealthRecordMedia>
    ) {
        // Add to in-memory cache (temporary)
        if (!mediaCache.containsKey(recordId)) {
            mediaCache[recordId] = mutableListOf()
        }
        mediaCache[recordId]?.addAll(media)
        
        // Update the health record object too
        healthRecordsCache[recordId]?.let { record ->
            val updatedMediaList = record.mediaItems.toMutableList()
            updatedMediaList.addAll(media)
            healthRecordsCache[recordId] = record.copy(mediaItems = updatedMediaList)
        }
        
        // TODO: Implement Room database insertion
        // Example:
        // healthRecordMediaDao.insertAll(media.map { it.toHealthRecordMediaEntity() })
    }
    
    override suspend fun getMediaForHealthRecord(recordId: Int): List<HealthRecordMedia> {
        // Return from in-memory cache (temporary)
        return mediaCache[recordId] ?: emptyList()
        
        // TODO: Implement Room database query
        // Example:
        // return healthRecordMediaDao.getMediaForHealthRecord(recordId)
        //     .map { it.toHealthRecordMedia() }
    }
    
    override suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int) {
        // Remove from in-memory cache (temporary)
        mediaCache[recordId]?.removeAll { it.id == mediaId }
        
        // Update the health record object too
        healthRecordsCache[recordId]?.let { record ->
            val updatedMediaList = record.mediaItems.filter { it.id != mediaId }
            healthRecordsCache[recordId] = record.copy(mediaItems = updatedMediaList)
        }
        
        // Also delete media file if it exists
        val mediaFile = File(File(healthRecordMediaDir, recordId.toString()), "$mediaId.media")
        if (mediaFile.exists()) {
            mediaFile.delete()
        }
        
        // TODO: Implement Room database deletion
        // Example:
        // healthRecordMediaDao.deleteMedia(mediaId)
    }
    
    override suspend fun getUpcomingReminders(daysAhead: Int): List<HealthRecord> {
        val cutoffTime = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(daysAhead.toLong()))
        
        // Filter from in-memory cache (temporary)
        return healthRecordsCache.values
            .filter { it.nextReminderDate != null && it.nextReminderDate.before(cutoffTime) }
            .sortedBy { it.nextReminderDate }
        
        // TODO: Implement Room database query
        // Example:
        // val cutoffDate = SimpleDateFormat("yyyy-MM-dd").format(cutoffTime)
        // return healthRecordDao.getUpcomingReminders(cutoffDate)
        //     .map { it.toHealthRecord() }
    }
    
    override suspend fun clearOutdatedRecords(olderThan: Long) {
        val cutoffTime = Date(olderThan)
        
        // Filter from in-memory cache (temporary)
        val recordsToRemove = healthRecordsCache.values
            .filter { it.recordDate.before(cutoffTime) }
            .mapNotNull { it.id }
        
        recordsToRemove.forEach { recordId ->
            healthRecordsCache.remove(recordId)
            mediaCache.remove(recordId)
        }
        
        // TODO: Implement Room database deletion
        // Example:
        // val cutoffDate = SimpleDateFormat("yyyy-MM-dd").format(cutoffTime)
        // healthRecordDao.deleteRecordsOlderThan(cutoffDate)
    }
}
