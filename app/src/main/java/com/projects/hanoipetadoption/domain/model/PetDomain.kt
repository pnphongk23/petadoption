package com.projects.hanoipetadoption.domain.model

/**
 * Domain model for Pet entity
 */
data class PetDomain(
    val id: String,
    val name: String,
    val breed: String,
    val gender: GenderDomain,
    val age: String,
    val weight: String,
    val characteristics: List<String>,
    val description: String,
    val healthStatus: Map<String, Boolean>,
    val adoptionRequirements: List<String>,
    val imageRes: String,
    val isFavorite: Boolean
)

enum class GenderDomain {
    MALE, FEMALE, UNKNOWN;
    
    val displayName: String
        get() = when (this) {
            MALE -> "Đực"
            FEMALE -> "Cái"
            UNKNOWN -> "Không rõ"
        }
}
