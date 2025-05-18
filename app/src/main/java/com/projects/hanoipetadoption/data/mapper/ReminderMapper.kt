package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.local.entity.ReminderEntity
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.data.model.postadoption.ReminderType
import java.util.Date

/**
 * Maps HealthRecord (domain model for local source interface) to ReminderEntity (database entity).
 */
fun HealthRecord.toReminderEntity(): ReminderEntity {
    val title = when (this.recordType) {
        RecordType.VACCINATION -> "Vaccination Reminder"
        RecordType.TREATMENT -> "Treatment Reminder"
        RecordType.WEIGHT -> "Weight Check Reminder"
        RecordType.GENERAL -> "General Pet Reminder"
        // else case is not strictly needed if RecordType enum is stable and all values are covered.
        // However, if RecordType can expand, an else might be safer for default handling.
    } + (this.petId.let { " for Pet $it" } ?: "")

    return ReminderEntity(
        id = this.id?.toLong() ?: 0L,
        petId = this.petId,
        title = this.notes?.substringBefore('\n')?.take(100) ?: title,
        description = this.notes,
        dueDate = this.nextReminderDate ?: Date(),
        reminderType = mapRecordTypeToReminderType(this.recordType),
        isCompleted = this.nextReminderDate == null,
        createdAt = this.recordDate
    )
}

/**
 * Maps ReminderEntity (database entity) to HealthRecord (domain model for local source interface).
 */
fun ReminderEntity.toHealthRecord(): HealthRecord {
    return HealthRecord(
        id = this.id.toInt(),
        petId = this.petId,
        userId = null,
        recordType = mapReminderTypeToRecordType(this.reminderType),
        notes = "${this.title}${this.description?.let { "\n\nDescription:\n$it" } ?: ""}",
        weight = null,
        recordDate = this.createdAt,
        nextReminderDate = if (this.isCompleted) null else this.dueDate,
        mediaItems = emptyList()
    )
}

/**
 * Maps HealthRecord to the Reminder data class.
 */
fun HealthRecord.toReminder(): Reminder {
    // Determine title for the Reminder model.
    // This might be similar to how it's done for ReminderEntity or could be simpler
    // if HealthRecord.notes is expected to be the primary source for description.
    val reminderTitle = when (this.recordType) {
        RecordType.VACCINATION -> "Vaccination"
        RecordType.TREATMENT -> "Treatment"
        RecordType.WEIGHT -> "Weight Check"
        RecordType.GENERAL -> "General Task"
        // Add else or ensure exhaustiveness if RecordType can expand
    } + (this.petId.let { " for Pet $it" } ?: "")

    return Reminder(
        id = this.id, // HealthRecord.id is Int?
        petId = this.petId,
        // Reminder.title is not nullable, ensure a value.
        // Prioritize notes' first line, then generated title, then default.
        title = this.notes?.substringBefore('\n')?.take(100) ?: reminderTitle,
        description = this.notes, // Reminder.description is String?
        // Reminder.dueDate is not nullable. Use nextReminderDate or recordDate as fallback.
        dueDate = this.nextReminderDate ?: this.recordDate, 
        reminderType = mapRecordTypeToReminderType(this.recordType),
        isCompleted = this.nextReminderDate == null // Infer completion status
    )
}

/**
 * Maps a list of HealthRecord objects to a list of Reminder objects.
 */
fun List<HealthRecord>.toReminderList(): List<Reminder> {
    return this.map { it.toReminder() }
}

fun List<ReminderEntity>.toHealthRecordList(): List<HealthRecord> {
    return this.map { it.toHealthRecord() }
}

fun List<HealthRecord>.toReminderEntityList(): List<ReminderEntity> {
    return this.map { it.toReminderEntity() }
}

private fun mapRecordTypeToReminderType(recordType: RecordType): ReminderType {
    return when (recordType) {
        RecordType.VACCINATION -> ReminderType.VACCINATION
        RecordType.TREATMENT -> ReminderType.MEDICATION
        RecordType.WEIGHT -> ReminderType.CHECKUP
        RecordType.GENERAL -> ReminderType.OTHER
    }
}

private fun mapReminderTypeToRecordType(reminderType: ReminderType): RecordType {
    return when (reminderType) {
        ReminderType.VACCINATION -> RecordType.VACCINATION
        ReminderType.MEDICATION -> RecordType.TREATMENT
        ReminderType.CHECKUP -> RecordType.GENERAL
        ReminderType.GROOMING -> RecordType.GENERAL
        ReminderType.OTHER -> RecordType.GENERAL
    }
}
