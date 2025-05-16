package com.projects.hanoipetadoption.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "adoption_applications")
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
