package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.data.model.postadoption.ReminderType

/**
 * Mapper functions to convert between HealthRecord and Reminder models
 */

/**
 * Convert HealthRecord to Reminder model
 */
fun HealthRecord.toReminder(): Reminder {
    // Determine reminder type based on record type
    val reminderType = when (this.recordType) {
        com.projects.hanoipetadoption.data.model.postadoption.RecordType.VACCINATION -> ReminderType.VACCINATION
        com.projects.hanoipetadoption.data.model.postadoption.RecordType.TREATMENT -> ReminderType.MEDICATION
        com.projects.hanoipetadoption.data.model.postadoption.RecordType.GENERAL -> ReminderType.CHECKUP
        com.projects.hanoipetadoption.data.model.postadoption.RecordType.WEIGHT -> ReminderType.OTHER
    }

    return Reminder(
        id = this.id,
        petId = this.petId,
        title = "Reminder: ${this.recordType.name}",
        description = this.notes,
        dueDate = this.nextReminderDate ?: this.recordDate,
        reminderType = reminderType,
        isCompleted = false
    )
}

/**
 * Convert a list of HealthRecord objects to a list of Reminder objects
 */
fun List<HealthRecord>.toReminderList(): List<Reminder> {
    return this.mapNotNull { healthRecord ->
        // Only include records that have a next reminder date
        healthRecord.nextReminderDate?.let {
            healthRecord.toReminder()
        }
    }
}
