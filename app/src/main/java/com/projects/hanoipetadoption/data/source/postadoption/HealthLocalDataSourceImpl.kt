package com.projects.hanoipetadoption.data.source.postadoption

import android.content.Context
import com.projects.hanoipetadoption.data.local.dao.HealthRecordDao
import com.projects.hanoipetadoption.data.mapper.toEntity
import com.projects.hanoipetadoption.data.mapper.toModel
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream

/**
 * Implementation of HealthLocalDataSource that uses Room database and file system
 */
class HealthLocalDataSourceImpl constructor(
    private val context: Context,
    private val healthRecordDao: HealthRecordDao
) : HealthLocalDataSource {
    
    private val healthRecordMediaDir: File by lazy {
        File(context.filesDir, "health_record_media").apply {
            if (!exists()) mkdirs()
        }
    }
    
    override suspend fun saveHealthRecords(records: List<HealthRecord>) {
        records.forEach { record ->
            saveHealthRecord(record)
        }
    }
    
    override suspend fun getHealthRecordsForPet(
        petId: String,
        recordType: String?
    ): List<HealthRecord> {
        val recordTypeEnum = recordType?.let { 
            try { RecordType.valueOf(it.uppercase()) } catch (e: IllegalArgumentException) { null } 
        }
        val flow = if (recordTypeEnum != null) {
            healthRecordDao.getHealthRecordsWithMediaForPetFilteredByType(petId, recordTypeEnum)
        } else {
            healthRecordDao.getHealthRecordsWithMediaForPet(petId)
        }
        return flow.map { list -> list.map { it.toModel() } }.firstOrNull() ?: emptyList()
    }
    
    override suspend fun getHealthRecord(recordId: Int): HealthRecord? {
        return healthRecordDao.getHealthRecordWithMediaById(recordId.toLong())?.toModel()
    }
    
    override suspend fun saveHealthRecord(record: HealthRecord) {
        val entity = record.toEntity()
        val savedEntityId = healthRecordDao.insertHealthRecord(entity)
        
        val effectiveId = record.id?.toLong() ?: savedEntityId
        
        healthRecordDao.deleteAllMediaForRecord(effectiveId)
        
        if (record.mediaItems.isNotEmpty()) {
            val mediaEntities = record.mediaItems.map {
                it.toEntity(effectiveId)
            }
            healthRecordDao.insertAllHealthRecordMedia(mediaEntities)
        }
    }
    
    override suspend fun deleteHealthRecord(recordId: Int) {
        val idAsLong = recordId.toLong()
        healthRecordDao.deleteHealthRecordById(idAsLong)
        
        val mediaDirForRecord = File(healthRecordMediaDir, idAsLong.toString())
        if (mediaDirForRecord.exists()) {
            mediaDirForRecord.deleteRecursively()
        }
    }
    
    override suspend fun saveMediaForHealthRecord(
        recordId: Int,
        media: List<HealthRecordMedia>
    ) {
        val recordIdAsLong = recordId.toLong()
        val recordSpecificMediaDir = File(healthRecordMediaDir, recordIdAsLong.toString()).apply {
            if (!exists()) mkdirs()
        }
        
        val mediaEntitiesToSave = media.mapNotNull { mediaItem ->
            try {
                val sourceFile = File(mediaItem.filePath)
                if (!sourceFile.exists()) {
                    println("Source file does not exist: ${mediaItem.filePath}")
                    return@mapNotNull null
                }
                val fileName = "media_${System.currentTimeMillis()}_${sourceFile.nameWithoutExtension}.${sourceFile.extension}"
                val destinationFile = File(recordSpecificMediaDir, fileName)
                
                sourceFile.inputStream().use { input ->
                    FileOutputStream(destinationFile).use { output ->
                        input.copyTo(output)
                    }
                }
                mediaItem.copy(filePath = destinationFile.absolutePath).toEntity(recordIdAsLong)
            } catch (e: Exception) {
                println("Error saving media file ${mediaItem.filePath}: ${e.message}")
                null
            }
        }
        if (mediaEntitiesToSave.isNotEmpty()) {
            healthRecordDao.insertAllHealthRecordMedia(mediaEntitiesToSave)
        }
    }
    
    override suspend fun getMediaForHealthRecord(recordId: Int): List<HealthRecordMedia> {
        return healthRecordDao.getMediaForHealthRecord(recordId.toLong()).map { it.toModel() }
    }
    
    override suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int) {
        val mediaEntity = healthRecordDao.getMediaForHealthRecord(recordId.toLong())
            .find { it.id == mediaId.toLong() }
        
        mediaEntity?.let {
            val fileToDelete = File(it.filePath)
            if (fileToDelete.exists()) {
                fileToDelete.delete()
            }
            healthRecordDao.deleteMediaById(it.id)
        }
    }
    
    override suspend fun getUpcomingReminders(daysAhead: Int): List<HealthRecord> {
        println("HealthLocalDataSourceImpl.getUpcomingReminders is a placeholder and needs proper implementation strategy aligned with ReminderRepository.")
        return emptyList()
    }
    
    override suspend fun clearOutdatedRecords(olderThan: Long) {
        throw NotImplementedError("clearOutdatedRecords not implemented yet.")
    }
}
