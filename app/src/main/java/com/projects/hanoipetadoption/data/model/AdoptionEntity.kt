package com.projects.hanoipetadoption.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Entity class for adoption applications with optimized indexing and relationships
 */
@Entity(
    tableName = "adoption_applications",
    indices = [
        Index(value = ["petId"]),
        Index(value = ["status"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AdoptionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val petId: String,
    val petName: String,
    val applicantName: String,
    val phoneNumber: String,
    val email: String,
    val address: String,
    val petExperience: String,
    val livingSpace: String,
    val hoursAtHome: String,
    val applicationDate: Long = System.currentTimeMillis(),
    val status: String = "APPROVED" // Auto-approved as per requirement
)
