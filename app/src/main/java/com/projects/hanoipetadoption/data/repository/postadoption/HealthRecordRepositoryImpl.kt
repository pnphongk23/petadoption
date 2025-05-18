package com.projects.hanoipetadoption.data.repository.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.MediaType
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.data.source.postadoption.HealthLocalDataSource
import com.projects.hanoipetadoption.data.util.Result
import com.projects.hanoipetadoption.domain.repository.postadoption.HealthRecordRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Implementation of HealthRecordRepository
 */
class HealthRecordRepositoryImpl(
    private val localDataSource: HealthLocalDataSource
) : HealthRecordRepository {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    
    override suspend fun createHealthRecord(healthRecord: HealthRecord): Result<HealthRecord> {
        return try {
            localDataSource.saveHealthRecord(healthRecord)
            Result.success(healthRecord)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun getHealthRecordsForPet(
        petId: String,
        recordType: RecordType?,
        startDate: Date?,
        endDate: Date?
    ): Result<List<HealthRecord>> {
        return try {
            val records = localDataSource.getHealthRecordsForPet(
                petId = petId,
                recordType = recordType?.name
            )
            Result.success(records)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun getHealthRecord(recordId: Int): Result<HealthRecord> {
        return try {
            val record = localDataSource.getHealthRecord(recordId)
            if (record == null) {
                Result.error(Exception("Health record with ID $recordId not found"))
            } else {
                Result.success(record)
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun updateHealthRecord(recordId: Int, healthRecord: HealthRecord): Result<HealthRecord> {
        return try {
            val updatedRecord = (healthRecord.copy(id = recordId))
            localDataSource.saveHealthRecord(updatedRecord)
            Result.success(updatedRecord)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun deleteHealthRecord(recordId: Int): Result<Unit> {
        return try {
            localDataSource.deleteHealthRecord(recordId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun addMediaToHealthRecord(
        recordId: Int,
        mediaFile: File,
        mediaType: String
    ): Result<HealthRecordMedia> {
        return try {
            val newMediaItem = HealthRecordMedia(
                id = 0,
                healthRecordId = recordId,
                filePath = mediaFile.absolutePath,
                mediaType = MediaType.IMAGE
            )
            val savedMediaList = localDataSource.saveMediaForHealthRecord(recordId, listOf(newMediaItem))
            Result.success(newMediaItem)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun getMediaForHealthRecord(recordId: Int): Result<List<HealthRecordMedia>> {
        return try {
            val mediaList = localDataSource.getMediaForHealthRecord(recordId)
            Result.success(mediaList)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int): Result<Unit> {
        return try {
            localDataSource.deleteMediaFromHealthRecord(recordId, mediaId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun getUpcomingReminders(daysAhead: Int): Result<List<HealthRecord>> {
        return try {
            val reminders = localDataSource.getUpcomingReminders(daysAhead)
            Result.success(reminders)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun createVaccinationReminder(
        petId: String,
        name: String,
        notes: String?,
        reminderDate: Date,
        isRecurring: Boolean,
        recurrenceIntervalDays: Int?
    ): Result<HealthRecord> {
        return try {
            val reminderDto = VaccinationReminderCreate(
                petId = petId,
                name = name,
                notes = notes,
                reminderDate = reminderDate,
                isRecurring = isRecurring,
                recurrenceIntervalDays = recurrenceIntervalDays
            )
            val createdRecord = localDataSource.createVaccinationReminder(reminderDto)
            Result.success(createdRecord)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun clearOutdatedHealthRecords(olderThan: Long): Result<Unit> {
        return try {
            localDataSource.clearOutdatedRecords(olderThan)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Helper function to format dates for API calls or local storage if needed in specific format.
     */
    private fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
}
