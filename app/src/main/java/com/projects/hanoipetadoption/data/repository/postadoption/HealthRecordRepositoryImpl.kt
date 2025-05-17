package com.projects.hanoipetadoption.data.repository.postadoption

import com.projects.hanoipetadoption.data.mapper.toHealthRecord
import com.projects.hanoipetadoption.data.mapper.toHealthRecordCreate
import com.projects.hanoipetadoption.data.mapper.toHealthRecordList
import com.projects.hanoipetadoption.data.mapper.toHealthRecordMedia
import com.projects.hanoipetadoption.data.mapper.toHealthRecordUpdate
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.data.source.postadoption.HealthRecordRemoteDataSource
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
    private val remoteDataSource: HealthRecordRemoteDataSource
) : HealthRecordRepository {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    
    override suspend fun createHealthRecord(healthRecord: HealthRecord): Result<HealthRecord> {
        return try {
            val record = remoteDataSource.createHealthRecord(healthRecord.toHealthRecordCreate())
            Result.success(record.toHealthRecord())
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
            val records = remoteDataSource.getHealthRecordsForPet(
                petId = petId,
                recordType = recordType?.name,
                startDate = startDate?.let { formatDate(it) },
                endDate = endDate?.let { formatDate(it) }
            )
            Result.success(records.toHealthRecordList())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun getHealthRecord(recordId: Int): Result<HealthRecord> {
        return try {
            val record = remoteDataSource.getHealthRecord(recordId)
            Result.success(record.toHealthRecord())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun updateHealthRecord(recordId: Int, healthRecord: HealthRecord): Result<HealthRecord> {
        return try {
            val record = remoteDataSource.updateHealthRecord(
                recordId = recordId,
                update = healthRecord.toHealthRecordUpdate()
            )
            Result.success(record.toHealthRecord())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun deleteHealthRecord(recordId: Int): Result<Unit> {
        return try {
            remoteDataSource.deleteHealthRecord(recordId)
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
            val media = remoteDataSource.addMediaToHealthRecord(
                recordId = recordId,
                file = mediaFile,
                mediaType = mediaType
            )
            Result.success(media.toHealthRecordMedia())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun getMediaForHealthRecord(recordId: Int): Result<List<HealthRecordMedia>> {
        return try {
            val mediaList = remoteDataSource.getMediaForHealthRecord(recordId)
            Result.success(mediaList.map { it.toHealthRecordMedia() })
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int): Result<Unit> {
        return try {
            remoteDataSource.deleteMediaFromHealthRecord(recordId, mediaId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun getUpcomingReminders(daysAhead: Int): Result<List<HealthRecord>> {
        return try {
            val reminders = remoteDataSource.getUpcomingReminders(daysAhead)
            Result.success(reminders.toHealthRecordList())
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
            val reminder = VaccinationReminderCreate(
                petId = petId,
                name = name,
                notes = notes,
                reminderDate = formatDate(reminderDate),
                isRecurring = isRecurring,
                recurrenceIntervalDays = recurrenceIntervalDays
            )
            val response = remoteDataSource.createVaccinationReminder(reminder)
            Result.success(response.toHealthRecord())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Helper function to format dates for API calls
     */
    private fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
}
