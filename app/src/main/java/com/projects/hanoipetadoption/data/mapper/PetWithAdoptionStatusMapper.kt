package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.model.PetWithAdoptionStatusEntity
import com.projects.hanoipetadoption.domain.model.GenderDomain
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.ui.model.PetWithAdoptionStatus

/**
 * Mapper to convert PetWithAdoptionStatusEntity to domain and presentation models
 */
fun PetWithAdoptionStatusEntity.toDomain(): Pair<PetDomain, Boolean> {
    // First convert the embedded PetEntity to PetDomain
    val petDomain = with(pet) {
        PetDomain(
            id = id,
            name = name,
            breed = breed,
            gender = when (gender) {
                "MALE" -> GenderDomain.MALE
                "FEMALE" -> GenderDomain.FEMALE
                else -> GenderDomain.UNKNOWN
            },
            age = age,
            weight = weight ?: "Unknown",
            characteristics = listOf(), // These would be populated from a separate query if needed
            description = description,
            healthStatus = mapOf(), // These would be populated from a separate query if needed
            adoptionRequirements = listOf(), // These would be populated from a separate query if needed
            imageRes = imageRes,
            isFavorite = isFavorite
        )
    }
    
    // Return the domain model along with the adoption status
    return Pair(petDomain, isAdopted)
} 