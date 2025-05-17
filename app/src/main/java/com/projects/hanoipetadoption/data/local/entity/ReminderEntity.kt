package com.projects.hanoipetadoption.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.projects.hanoipetadoption.data.model.postadoption.ReminderType
import java.util.Date
import com.projects.hanoipetadoption.data.model.PetEntity

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["pet_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["pet_id"])]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "pet_id")
    val petId: String,

    val title: String,
    val description: String? = null,

    @ColumnInfo(name = "due_date")
    val dueDate: Date,

    @ColumnInfo(name = "reminder_type")
    val reminderType: ReminderType,

    @ColumnInfo(name = "is_completed", defaultValue = "0")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date() 
    // Removed is_recurring and recurrence_interval_days as they were not in Reminder.kt
    // Add them if Reminder model gets these fields
) 