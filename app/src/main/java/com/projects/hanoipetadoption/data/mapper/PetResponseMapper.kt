// filepath: e:\Git\hanoipetadoption\app\src\main\java\com\projects\hanoipetadoption\data\mapper\PetResponseMapper.kt
package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.model.PetData
import com.projects.hanoipetadoption.data.model.PetResponse
import com.projects.hanoipetadoption.ui.model.PetGender

/**
 * Mapper functions to convert between API response models and data layer models
 */

fun PetResponse.toPetData(): PetData {
    return PetData(
        id = id.toString(),
        name = name,
        category = type.uppercase(),
        breed = breed ?: "",
        age = calculateAgeDisplay(age),
        gender = mapGender(gender),
        description = description ?: "",
        imageRes = imageUrl ?: "",
        isFavorite = false, // Default value, could be stored locally
        characteristics = details?.split(",")?.map { it.trim() } ?: emptyList(),
        weight = "", // Not provided in API
        healthStatus = emptyMap(), // Not provided in API
        adoptionRequirements = emptyList() // Not provided in API
    )
}

// Helper functions
private fun calculateAgeDisplay(ageInMonths: Int?): String {
    if (ageInMonths == null) return "Không rõ"
    
    return when {
        ageInMonths < 3 -> "Sơ sinh" 
        ageInMonths < 12 -> "Nhí"
        ageInMonths < 24 -> "Nhỡ"
        else -> "Trưởng thành"
    }
}

private fun mapGender(gender: String?): PetGender {
    return when (gender?.lowercase()) {
        "male" -> PetGender.MALE
        "female" -> PetGender.FEMALE
        else -> PetGender.MALE // Default if unknown
    }
}

fun List<PetResponse>.toPetDataList(): List<PetData> {
    return this.map { it.toPetData() }
}
