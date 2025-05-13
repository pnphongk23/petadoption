package com.projects.hanoipetadoption.data.model.postadoption

import java.util.Date

/**
 * Enum representing the types of health records
 */
enum class RecordType {
    GENERAL, VACCINATION, TREATMENT, WEIGHT
}

/**
 * Enum representing the types of media attachments
 */
enum class MediaType {
    IMAGE, VIDEO, DOCUMENT
}

/**
 * Data class for health record model
 */
data class HealthRecord(
    val id: Int? = null,
    val petId: Int,
    val userId: Int? = null,
    val recordType: RecordType,
    val notes: String? = null,
    val weight: Double? = null,
    val recordDate: Date = Date(),
    val nextReminderDate: Date? = null,
    val mediaItems: List<HealthRecordMedia> = emptyList()
)

/**
 * Data class for health record media
 */
data class HealthRecordMedia(
    val id: Int? = null,
    val healthRecordId: Int? = null,
    val mediaType: MediaType,
    val filePath: String,
    val uploadDate: Date = Date()
)

/**
 * Data class for creating a health record
 */
data class HealthRecordCreate(
    val petId: Int,
    val recordType: RecordType,
    val notes: String? = null,
    val weight: Double? = null,
    val nextReminderDate: Date? = null
)

/**
 * Data class for updating a health record
 */
data class HealthRecordUpdate(
    val recordType: RecordType,
    val notes: String? = null,
    val weight: Double? = null,
    val nextReminderDate: Date? = null
)
