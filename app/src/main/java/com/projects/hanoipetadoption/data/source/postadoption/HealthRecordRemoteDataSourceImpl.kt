package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMediaResponse
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordResponse
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordUpdate
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.data.network.PostAdoptionApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Implementation of HealthRecordRemoteDataSource that uses Retrofit API service
 */
class HealthRecordRemoteDataSourceImpl(
    private val apiService: PostAdoptionApiService
) : HealthRecordRemoteDataSource {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    
    override suspend fun createHealthRecord(record: HealthRecordCreate): HealthRecordResponse {
        return apiService.createHealthRecord(record)
    }
    
    override suspend fun getHealthRecordsForPet(
        petId: Int,
        recordType: String?,
        startDate: String?,
        endDate: String?
    ): List<HealthRecordResponse> {
        return apiService.getHealthRecordsForPet(
            petId = petId,
            recordType = recordType,
            startDate = startDate,
            endDate = endDate
        )
    }
    
    override suspend fun getHealthRecord(recordId: Int): HealthRecordResponse {
        return apiService.getHealthRecord(recordId)
    }
    
    override suspend fun updateHealthRecord(
        recordId: Int,
        update: HealthRecordUpdate
    ): HealthRecordResponse {
        return apiService.updateHealthRecord(recordId, update)
    }
    
    override suspend fun deleteHealthRecord(recordId: Int) {
        apiService.deleteHealthRecord(recordId)
    }
    
    override suspend fun addMediaToHealthRecord(
        recordId: Int,
        file: File,
        mediaType: String
    ): HealthRecordMediaResponse {
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val mediaTypePart = mediaType.toRequestBody("text/plain".toMediaTypeOrNull())
        
        return apiService.addMediaToHealthRecord(recordId, filePart, mediaTypePart)
    }
    
    override suspend fun getMediaForHealthRecord(recordId: Int): List<HealthRecordMediaResponse> {
        return apiService.getMediaForHealthRecord(recordId)
    }
    
    override suspend fun deleteMediaFromHealthRecord(recordId: Int, mediaId: Int) {
        apiService.deleteMediaFromHealthRecord(recordId, mediaId)
    }
    
    override suspend fun getUpcomingReminders(daysAhead: Int): List<HealthRecordResponse> {
        return apiService.getUpcomingReminders(daysAhead)
    }
    
    override suspend fun createVaccinationReminder(
        reminder: VaccinationReminderCreate
    ): HealthRecordResponse {
        return apiService.createVaccinationReminder(reminder)
    }
    
    /**
     * Helper function to format dates for API calls
     */
    private fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
}
