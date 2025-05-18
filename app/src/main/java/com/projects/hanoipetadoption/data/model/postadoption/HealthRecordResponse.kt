package com.projects.hanoipetadoption.data.model.postadoption

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * API response model for health records
 */
data class HealthRecordResponse(
    val id: Int,
    @SerializedName("pet_id")
    val petId: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("record_type")
    val recordType: String,
    val notes: String?,
    val weight: Double?,
    @SerializedName("record_date")
    val recordDate: String,
    @SerializedName("next_reminder_date")
    val nextReminderDate: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val media: List<HealthRecordMediaResponse>?
)

/**
 * API response model for health record media items
 */
data class HealthRecordMediaResponse(
    val id: Int,
    @SerializedName("health_record_id")
    val healthRecordId: Int,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("file_path")
    val filePath: String,
    @SerializedName("upload_date")
    val uploadDate: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String?
)

/**
 * API response model for vaccination reminder create
 */
data class VaccinationReminderCreate(
    @SerializedName("pet_id")
    val petId: String,
    val name: String,
    val notes: String?,
    @SerializedName("reminder_date")
    val reminderDate: Date,
    @SerializedName("is_recurring")
    val isRecurring: Boolean = false,
    @SerializedName("recurrence_interval_days")
    val recurrenceIntervalDays: Int? = null
)
