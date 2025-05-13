package com.projects.hanoipetadoption.data.model.postadoption

import java.util.Date

/**
 * Enum representing the types of reminders
 */
enum class ReminderType {
    VACCINATION, MEDICATION, CHECKUP, GROOMING, OTHER
}

/**
 * Data class for reminder model
 */
data class Reminder(
    val id: Int? = null,
    val petId: Int,
    val title: String,
    val description: String? = null,
    val dueDate: Date,
    val reminderType: ReminderType,
    val isCompleted: Boolean = false
)
