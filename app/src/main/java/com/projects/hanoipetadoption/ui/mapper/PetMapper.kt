package com.projects.hanoipetadoption.ui.mapper

import com.projects.hanoipetadoption.domain.model.GenderDomain
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetGender

/**
 * Maps domain models to UI models
 */
fun PetDomain.toUiModel(): Pet {

    // Map gender
    val uiGender = when (gender) {
        GenderDomain.MALE -> PetGender.MALE
        GenderDomain.FEMALE -> PetGender.FEMALE
        else -> PetGender.MALE // Default, since UI model lacks UNKNOWN
    }
    
    return Pet(
        id = id,
        name = name,
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