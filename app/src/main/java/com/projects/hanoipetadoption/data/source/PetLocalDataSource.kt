package com.projects.hanoipetadoption.data.source

import com.projects.hanoipetadoption.data.model.PetData

/**
 * Local data source interface for accessing pet data
 */
interface PetLocalDataSource {
    suspend fun getPetById(id: String): PetData?
    suspend fun toggleFavorite(id: String): Boolean
    suspend fun getAllPets(): List<PetData>
}
