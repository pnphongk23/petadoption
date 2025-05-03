package com.projects.hanoipetadoption.domain.repository

import com.projects.hanoipetadoption.domain.model.PetDomain

/**
 * Repository interface for pet operations
 */
interface PetRepository {
    suspend fun getPetById(id: String): Result<PetDomain>
    suspend fun toggleFavorite(id: String): Result<Boolean>
    suspend fun getAllPets(): Result<List<PetDomain>>
}
