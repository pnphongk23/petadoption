package com.projects.hanoipetadoption.data.network

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMediaResponse
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordResponse
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordUpdate
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import okhttp3.ResponseBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service interface for post-adoption tracking features
 */
interface PostAdoptionApiService {
    
    // Health Records
    @POST("api/health-records")
    suspend fun createHealthRecord(@Body record: HealthRecordCreate): HealthRecordResponse
    
    @GET("api/health-records/pet/{petId}")
    suspend fun getHealthRecordsForPet(
        @Path("petId") petId: String,
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100,
        @Query("record_type") recordType: String? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null
    ): List<HealthRecordResponse>
    
    @GET("api/health-records/{recordId}")
    suspend fun getHealthRecord(@Path("recordId") recordId: Int): HealthRecordResponse
    
    @PUT("api/health-records/{recordId}")
    suspend fun updateHealthRecord(
        @Path("recordId") recordId: Int,
        @Body update: HealthRecordUpdate
    ): HealthRecordResponse
    
    @DELETE("api/health-records/{recordId}")
    suspend fun deleteHealthRecord(@Path("recordId") recordId: Int)
    
    @Multipart
    @POST("api/health-records/{recordId}/media")
    suspend fun addMediaToHealthRecord(
        @Path("recordId") recordId: Int,
        @Part file: MultipartBody.Part,
        @Part("media_type") mediaType: RequestBody
    ): HealthRecordMediaResponse
    
    @GET("api/health-records/{recordId}/media")
    suspend fun getMediaForHealthRecord(
        @Path("recordId") recordId: Int
    ): List<HealthRecordMediaResponse>
    
    @DELETE("api/health-records/{recordId}/media/{mediaId}")
    suspend fun deleteMediaFromHealthRecord(
        @Path("recordId") recordId: Int,
        @Path("mediaId") mediaId: Int
    )
    
    // Reminders
    @GET("api/reminders/upcoming")
    suspend fun getUpcomingReminders(
        @Query("days_ahead") daysAhead: Int = 7
    ): List<HealthRecordResponse>
      @POST("api/reminders/vaccination")
    suspend fun createVaccinationReminder(
        @Body reminder: VaccinationReminderCreate
    ): HealthRecordResponse
    
    // Care instructions endpoints
    @GET("api/care-instructions/pet/{petId}")
    suspend fun getCareInstructionsForPet(
        @Path("petId") petId: String
    ): List<Map<String, Any>>
    
    @GET("api/care-instructions/{instructionId}")
    suspend fun getCareInstructionById(
        @Path("instructionId") instructionId: Int
    ): Map<String, Any>
    
    @GET("api/care-instructions/{instructionId}/download")
    suspend fun downloadCareInstructionDocument(
        @Path("instructionId") instructionId: Int
    ): ResponseBody
}
