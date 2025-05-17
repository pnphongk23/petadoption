// filepath: e:\Git\hanoipetadoption\app\src\main\java\com\projects\hanoipetadoption\data\repository\PetRepositoryImpl.kt
package com.projects.hanoipetadoption.data.repository

import com.projects.hanoipetadoption.data.mapper.toDomain
import com.projects.hanoipetadoption.data.mapper.toPetDataList
import com.projects.hanoipetadoption.data.mapper.toPresentation
import com.projects.hanoipetadoption.data.source.PetLocalDataSource
import com.projects.hanoipetadoption.data.source.PetRemoteDataSource
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.domain.repository.PetRepository
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetWithAdoptionStatus
import com.projects.hanoipetadoption.util.ConnectivityChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Implementation of the PetRepository interface
 * Uses both remote and local data sources with a fallback mechanism
 */
class PetRepositoryImpl(
    private val remoteDataSource: PetRemoteDataSource,
    private val localDataSource: PetLocalDataSource,
    private val connectivityChecker: ConnectivityChecker
) : PetRepository {

    override suspend fun getPetById(id: String): Result<PetDomain> = withContext(Dispatchers.IO) {
        try {
            if (connectivityChecker.isNetworkAvailable()) {
                // First try to get from remote source
                val remotePet = remoteDataSource.getPetById(id)
                // Cache the result in local database
                localDataSource.insertPet(remotePet)
                Result.success(remotePet.toDomain())
            } else {
                // Use local data when offline
                val localPet = localDataSource.getPetById(id)
                if (localPet != null) {
                    Result.success(localPet.toDomain())
                } else {
                    Result.failure(NoSuchElementException("Pet not found with id: $id"))
                }
            }
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
            if (connectivityChecker.isNetworkAvailable()) {
                // Try to get from remote source first
                val response = remoteDataSource.getAllPets()
                val pets = response.pets.toPetDataList()
                
                // Cache data locally
                localDataSource.insertPets(pets)
                
                Result.success(pets.map { it.toDomain() })
            } else {
                // Use local data when offline
                val pets = localDataSource.getAllPets()
                Result.success(pets.map { it.toDomain() })
            }
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
    
    /**
     * Gets all pets with their adoption status using a JOIN query
     * This is more efficient than getting pets and adoption status separately
     */
    override fun getAllPetsWithAdoptionStatus(): Flow<Result<List<PetWithAdoptionStatus>>> {
        return localDataSource.getAllPetsWithAdoptionStatus()
            .map { petEntitiesWithStatus ->
                // Map entity list to UI model list
                val uiModelList = petEntitiesWithStatus.map { entityWithStatus ->
                    val petDomain = entityWithStatus.toDomain()
                    // Map to UI model
                    PetWithAdoptionStatus(
                        pet = petDomain.first.toPresentation(),
                        isAdopted = petDomain.second
                    )
                }
                Result.success(uiModelList)
            }
            .catch { error ->
                // Handle any errors in the flow
                emit(Result.failure(error))
            }
    }
}
