package com.projects.hanoipetadoption.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pet_status_update_images",
    foreignKeys = [
        ForeignKey(
            entity = PetStatusUpdateEntity::class,
            parentColumns = ["id"],
            childColumns = ["status_update_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["status_update_id"])]
)
data class PetStatusUpdateImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "status_update_id")
    val statusUpdateId: Long, // Foreign key to PetStatusUpdateEntity

    @ColumnInfo(name = "image_url")
    val imageUrl: String // Local file path or remote URL after saving/uploading
) 