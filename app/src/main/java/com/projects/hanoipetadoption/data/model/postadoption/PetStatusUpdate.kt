package com.projects.hanoipetadoption.data.model.postadoption

import java.util.Date

/**
 * Data class for pet status update
 */
data class PetStatusUpdate(
    val id: Int? = null,
    val petId: String,
    val userId: Int? = null,
    val description: String,
    val imageUrls: List<String>? = null,
    val createdAt: Date = Date(),
    val milestone: String? = null
)