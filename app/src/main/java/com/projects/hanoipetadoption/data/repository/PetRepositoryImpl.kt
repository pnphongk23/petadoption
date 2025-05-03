// filepath: e:\Git\hanoipetadoption\app\src\main\java\com\projects\hanoipetadoption\data\repository\PetRepositoryImpl.kt
package com.projects.hanoipetadoption.data.repository

import com.projects.hanoipetadoption.data.mapper.toDomain
import com.projects.hanoipetadoption.data.mapper.toPetDataList
import com.projects.hanoipetadoption.data.source.PetLocalDataSource
import com.projects.hanoipetadoption.data.source.PetRemoteDataSource
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.domain.repository.PetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Implementation of the PetRepository interface
 * Uses both remote and local data sources with a fallback mechanism
 */
class PetRepositoryImpl(
    private val remoteDataSource: PetRemoteDataSource,
    private val localDataSource: PetLocalDataSource,
    // Dependency to check for network connectivity could be added here
) : PetRepository {

    override suspend fun getPetById(id: String): Result<PetDomain> = withContext(Dispatchers.IO) {
        try {
            // First try to get from remote source
            val remotePet = remoteDataSource.getPetById(id)
            Result.success(remotePet.toDomain())
        } catch (e: Exception) {
            try {
                // If remote fails, try to get from local source
                val localPet = localDataSource.getPetById(id)
                if (localPet != null) {
                    Result.success(localPet.toDomain())
                } else {
                    Result.failure(NoSuchElementException("Pet not found with id: $id"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun toggleFavorite(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val success = localDataSource.toggleFavorite(id)
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllPets(): Result<List<PetDomain>> = withContext(Dispatchers.IO) {
        try {
            // Try to get from remote source first
            val response = remoteDataSource.getAllPets()
            val pets = response.pets.toPetDataList()
            Result.success(pets.map { it.toDomain() })
        } catch (e: IOException) {
            // If network error, fall back to local data
            try {
                val pets = localDataSource.getAllPets()
                Result.success(pets.map { it.toDomain() })
            } catch (e: Exception) {
                Result.failure(e)
            }
        } catch (e: Exception) {
            // For other errors, just return failure
            Result.failure(e)
        }
    }
}
