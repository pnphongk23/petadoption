package com.projects.hanoipetadoption.domain.repository

import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.ui.model.PetWithAdoptionStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for pet operations
 */
interface PetRepository {
    suspend fun getPetById(id: String): Result<PetDomain>
    suspend fun toggleFavorite(id: String): Result<Boolean>
    suspend fun getAllPets(): Result<List<PetDomain>>
    
    /**
     * Gets all pets with their adoption status in a single query
     * @return Flow of list of pets with their adoption status
     */
    fun getAllPetsWithAdoptionStatus(): Flow<Result<List<PetWithAdoptionStatus>>>
}
