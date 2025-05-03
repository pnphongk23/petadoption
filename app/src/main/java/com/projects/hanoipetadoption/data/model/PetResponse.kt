// filepath: e:\Git\hanoipetadoption\app\src\main\java\com\projects\hanoipetadoption\data\model\PetResponse.kt
package com.projects.hanoipetadoption.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

/**
 * API response model for a single pet
 */
data class PetResponse(
    val id: Int,
    val name: String,
    val type: String,
    val breed: String?,
    val gender: String?,
    val age: Int?,
    val description: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val status: String,
    val details: String?,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String?
)

/**
 * API response model for a list of pets
 */
data class PetListResponse(
    val pets: List<PetResponse>,
    val total: Int
)
