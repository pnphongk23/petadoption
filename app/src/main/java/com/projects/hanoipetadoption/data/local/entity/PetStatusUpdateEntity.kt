// PetStatusUpdateEntity.kt (Giả định dựa trên lần tạo trước)
package com.projects.hanoipetadoption.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.projects.hanoipetadoption.data.model.PetEntity // Đã được sửa để import đúng
import java.util.Date

@Entity(
    tableName = "pet_status_updates",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"], // Đã được sửa
            childColumns = ["pet_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["pet_id"]), Index(value = ["user_id"])]
)
data class PetStatusUpdateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Đã là Long, khớp
    @ColumnInfo(name = "pet_id")
    val petId: String, // Khớp
    @ColumnInfo(name = "user_id")
    val userId: Int? = null, // Khớp với PetStatusUpdate model (Int?)
    val description: String, // Khớp
    @ColumnInfo(name = "created_at")
    val createdAt: Date, // Khớp
    val milestone: String? = null // Khớp
    // imageUrls được xử lý bởi PetStatusUpdateImageEntity
)