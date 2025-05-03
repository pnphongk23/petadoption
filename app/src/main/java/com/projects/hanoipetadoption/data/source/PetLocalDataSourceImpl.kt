package com.projects.hanoipetadoption.data.source

import com.projects.hanoipetadoption.data.model.PetData
import com.projects.hanoipetadoption.ui.model.SamplePetsData

/**
 * Implementation of PetLocalDataSource that provides access to sample pet data
 */
class PetLocalDataSourceImpl : PetLocalDataSource {

    override suspend fun getPetById(id: String): PetData? {
        // Convert from UI model to data model
        return SamplePetsData.getPets().find { it.id == id }?.let {
            PetData(
                id = it.id,
                name = it.name,
                category = it.category.name,
                breed = it.breed,
                age = it.age,
                gender = it.gender,
                description = it.description,
                imageRes = it.imageRes,
                isFavorite = it.isFavorite,
                weight = "",
                healthStatus = emptyMap(),
                adoptionRequirements = emptyList(),
                characteristics = it.characteristics
            )
        }
    }

    override suspend fun toggleFavorite(id: String): Boolean {
        // In a real app, this would update a database
        // For now, just simulate success
        return true
    }

    override suspend fun getAllPets(): List<PetData> {
        // Convert from UI model to data model
        return SamplePetsData.getPets().map {
            PetData(
                id = it.id,
                name = it.name,
                category = it.category.name,
                breed = it.breed,
                age = it.age,
                gender = it.gender,
                description = it.description,
                imageRes = it.imageRes,
                isFavorite = it.isFavorite,
                characteristics = it.characteristics,
                weight = "",
                healthStatus = emptyMap(),
                adoptionRequirements = emptyList()
            )
        }
    }
}
