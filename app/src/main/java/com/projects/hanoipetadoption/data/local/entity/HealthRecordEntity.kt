package com.projects.hanoipetadoption.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import java.util.Date
import com.projects.hanoipetadoption.data.model.PetEntity

@Entity(
    tableName = "health_records",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["pet_id"],
            onDelete = ForeignKey.CASCADE
        )
        // We might also have a foreign key to a UserEntity if userId is important
        // ForeignKey(entity = UserEntity::class, parentColumns = ["userId"], childColumns = ["user_id"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index(value = ["pet_id"]), Index(value = ["user_id"])]
)
data class HealthRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "pet_id")
    val petId: String,

    @ColumnInfo(name = "user_id")
    val userId: Int? = null, // Assuming User ID is an Int. Change if it's String.

    @ColumnInfo(name = "record_type")
    val recordType: RecordType,

    val notes: String? = null,
    val weight: Double? = null,

    @ColumnInfo(name = "record_date")
    val recordDate: Date,

    @ColumnInfo(name = "next_reminder_date")
    val nextReminderDate: Date? = null
    // mediaItems will be handled by a separate table (HealthRecordMediaEntity) and a @Relation
) 