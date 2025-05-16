package com.projects.hanoipetadoption.ui.mapper

import com.projects.hanoipetadoption.domain.model.GenderDomain
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetCategory
import com.projects.hanoipetadoption.ui.model.PetGender

/**
 * Maps domain models to UI models
 */
fun PetDomain.toUiModel(): Pet {
    // Determine category based on breed or characteristics
    val category = when {
        breed.contains("cat", ignoreCase = true) || 
        characteristics.any { it.contains("cat", ignoreCase = true) } -> PetCategory.CAT
        
        breed.contains("dog", ignoreCase = true) || 
        characteristics.any { it.contains("dog", ignoreCase = true) } -> PetCategory.DOG
        
        breed.contains("bird", ignoreCase = true) || 
        characteristics.any { it.contains("bird", ignoreCase = true) } -> PetCategory.BIRD
        
        // Add more categorization logic if needed
        
        else -> PetCategory.OTHER
    }
    
    // Map gender
    val uiGender = when (gender) {
        GenderDomain.MALE -> PetGender.MALE
        GenderDomain.FEMALE -> PetGender.FEMALE
        else -> PetGender.MALE // Default, since UI model lacks UNKNOWN
    }
    
    return Pet(
        id = id,
        name = name,
        category = category,
        breed = breed,
        age = age,
        gender = uiGender,
        description = description,
        imageRes = imageRes,
        isFavorite = isFavorite,
        characteristics = characteristics
    )
}

/**
 * Maps a list of domain models to UI models
 */
fun List<PetDomain>.toUiModels(): List<Pet> = map { it.toUiModel() } 