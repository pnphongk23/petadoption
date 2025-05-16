package com.projects.hanoipetadoption.data.source

import com.projects.hanoipetadoption.data.model.PetData

/**
 * Local data source interface for accessing pet data
 */
interface PetLocalDataSource {
    suspend fun getPetById(id: String): PetData?
    suspend fun insertPet(petData: PetData)
    suspend fun insertPets(petDataList: List<PetData>)
    suspend fun updatePet(petData: PetData)
    suspend fun toggleFavorite(id: String): Boolean
    suspend fun getAllPets(): List<PetData>
    suspend fun deletePet(id: String)
}
