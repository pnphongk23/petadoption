package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.model.PetAdoptionRequirementEntity
import com.projects.hanoipetadoption.data.model.PetCharacteristicEntity
import com.projects.hanoipetadoption.data.model.PetData
import com.projects.hanoipetadoption.data.model.PetEntity
import com.projects.hanoipetadoption.data.model.PetHealthStatusEntity
import com.projects.hanoipetadoption.ui.model.PetGender

/**
 * Mappers for database entities to data models
 */

// Convert Entity to Data model
fun PetEntity.toPetData(
    characteristics: List<String>,
    healthStatus: Map<String, Boolean>,
    adoptionRequirements: List<String>
): PetData {
    return PetData(
        id = id,
        name = name,
        category = category,
        breed = breed,
        age = age,
        gender = when (gender) {
            "MALE" -> PetGender.MALE
            "FEMALE" -> PetGender.FEMALE
            else -> PetGender.MALE // Default
        },
        description = description,
        imageRes = imageRes,
        isFavorite = isFavorite,
        characteristics = characteristics,
        weight = weight ?: "",
        healthStatus = healthStatus,
        adoptionRequirements = adoptionRequirements
    )
}

// Convert Data model to Entity
fun PetData.toPetEntity(): PetEntity {
    return PetEntity(
        id = id,
        name = name,
        category = category,
        breed = breed,
        age = age,
        gender = gender.name,
        description = description,
        imageRes = imageRes,
        isFavorite = isFavorite,
        weight = weight
    )
}

// Convert Data model to Characteristic entities
fun PetData.toCharacteristicEntities(): List<PetCharacteristicEntity> {
    return characteristics.map {
        PetCharacteristicEntity(
            petId = id,
            characteristic = it
        )
    }
}

// Convert Data model to Health Status entities
fun PetData.toHealthStatusEntities(): List<PetHealthStatusEntity> {
    return healthStatus.map { (key, value) ->
        PetHealthStatusEntity(
            petId = id,
            statusName = key,
            statusValue = value
        )
    }
}

// Convert Data model to Adoption Requirement entities
fun PetData.toRequirementEntities(): List<PetAdoptionRequirementEntity> {
    return adoptionRequirements.map {
        PetAdoptionRequirementEntity(
            petId = id,
            requirement = it
        )
    }
} 