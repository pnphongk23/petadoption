package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordUpdate
import com.projects.hanoipetadoption.data.model.postadoption.MediaType
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.data.network.PostAdoptionApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Date

/**
 * Implementation of HealthRemoteDataSource that communicates with the remote server
 */
class HealthRemoteDataSourceImpl(
    private val apiService: PostAdoptionApiService
) : HealthRemoteDataSource {
    
    override suspend fun getHealthRecordsForPet(
        petId: Int, 
        recordType: RecordType?, 
        startDate: Date?, 
        endDate: Date?
    ): List<HealthRecord> {
        // In a real implementation, this would make an API call
        // For now, returning empty list as a mock implementation
        return emptyList()
    }

    override suspend fun getHealthRecordById(recordId: Int): HealthRecord? {
        // Mock implementation
        return null
    }

    override suspend fun createHealthRecord(healthRecord: HealthRecordCreate): HealthRecord {
        // Mock implementation
        return HealthRecord(
            id = 1,
            petId = healthRecord.petId,
            recordType = healthRecord.recordType,
            notes = healthRecord.notes,
            weight = healthRecord.weight,
            recordDate = Date(),
            nextReminderDate = healthRecord.nextReminderDate,
            mediaItems = emptyList()
        )
    }

    override suspend fun updateHealthRecord(recordId: Int, update: HealthRecordUpdate): HealthRecord {
        // Mock implementation
        return HealthRecord(
            id = recordId,
            petId = 1,
            recordType = update.recordType,
            notes = update.notes,
            weight = update.weight,
            recordDate = Date(),
            nextReminderDate = update.nextReminderDate,
            mediaItems = emptyList()
        )
    }

    override suspend fun deleteHealthRecord(recordId: Int): Boolean {
        // Mock implementation
        return true
    }

    override suspend fun addMediaToHealthRecord(
        recordId: Int, 
        file: File, 
        mediaType: MediaType
    ): HealthRecordMedia {
        // In a real implementation, we would create MultipartBody.Part
        // and use it to upload the file to the server
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val mediaTypePart = mediaType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        
        // Mock implementation that returns a dummy response
        return HealthRecordMedia(
            id = 1,
            healthRecordId = recordId,
            mediaType = mediaType,
            filePath = file.absolutePath,
            uploadDate = Date()
        )
    }

    override suspend fun getMediaForHealthRecord(recordId: Int): List<HealthRecordMedia> {
        // Mock implementation
        return emptyList()
    }

    override suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int): Boolean {
        // Mock implementation
        return true
    }
} 