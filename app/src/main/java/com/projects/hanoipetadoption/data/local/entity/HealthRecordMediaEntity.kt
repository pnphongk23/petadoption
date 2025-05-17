package com.projects.hanoipetadoption.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.projects.hanoipetadoption.data.model.postadoption.MediaType
import java.util.Date

@Entity(
    tableName = "health_record_media",
    foreignKeys = [
        ForeignKey(
            entity = HealthRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["health_record_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["health_record_id"])]
)
data class HealthRecordMediaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "health_record_id")
    val healthRecordId: Long, // Foreign key to HealthRecordEntity

    @ColumnInfo(name = "media_type")
    val mediaType: MediaType,

    @ColumnInfo(name = "file_path")
    val filePath: String, // Local file path after saving

    @ColumnInfo(name = "upload_date")
    val uploadDate: Date
) 