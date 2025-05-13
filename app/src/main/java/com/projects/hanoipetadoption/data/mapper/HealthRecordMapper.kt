package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.model.postadoption.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Mapper functions to convert between Health Record API response models and domain models
 */

private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

/**
 * Convert HealthRecordResponse to domain HealthRecord
 */
fun HealthRecordResponse.toHealthRecord(): HealthRecord {
    return HealthRecord(
        id = id,
        petId = petId,
        userId = userId,
        recordType = parseRecordType(recordType),
        notes = notes,
        weight = weight,
        recordDate = parseDate(recordDate),
        nextReminderDate = nextReminderDate?.let { parseDate(it) },
        mediaItems = media?.map { it.toHealthRecordMedia() } ?: emptyList()
    )
}

/**
 * Convert HealthRecordMediaResponse to domain HealthRecordMedia
 */
fun HealthRecordMediaResponse.toHealthRecordMedia(): HealthRecordMedia {
    return HealthRecordMedia(
        id = id,
        healthRecordId = healthRecordId,
        mediaType = parseMediaType(mediaType),
        filePath = filePath,
        uploadDate = parseDate(uploadDate)
    )
}

/**
 * Convert HealthRecord to HealthRecordCreate for API calls
 */
fun HealthRecord.toHealthRecordCreate(): HealthRecordCreate {
    return HealthRecordCreate(
        petId = petId,
        recordType = recordType,
        notes = notes,
        weight = weight,
        nextReminderDate = nextReminderDate
    )
}

/**
 * Convert HealthRecord to HealthRecordUpdate for API calls
 */
fun HealthRecord.toHealthRecordUpdate(): HealthRecordUpdate {
    return HealthRecordUpdate(
        recordType = recordType,
        notes = notes,
        weight = weight,
        nextReminderDate = nextReminderDate
    )
}

/**
 * Convert List of HealthRecordResponse to List of domain HealthRecord
 */
fun List<HealthRecordResponse>.toHealthRecordList(): List<HealthRecord> {
    return this.map { it.toHealthRecord() }
}

/**
 * Helper function to parse record type from string
 */
private fun parseRecordType(type: String): RecordType {
    return try {
        RecordType.valueOf(type.uppercase())
    } catch (e: IllegalArgumentException) {
        RecordType.GENERAL
    }
}

/**
 * Helper function to parse media type from string
 */
private fun parseMediaType(type: String): MediaType {
    return try {
        MediaType.valueOf(type.uppercase())
    } catch (e: IllegalArgumentException) {
        MediaType.IMAGE
    }
}

/**
 * Helper function to parse date from string
 */
private fun parseDate(dateStr: String): Date {
    return try {
        dateFormat.parse(dateStr) ?: Date()
    } catch (e: Exception) {
        Date()
    }
}
