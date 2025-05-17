package com.projects.hanoipetadoption.data.repository.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import com.projects.hanoipetadoption.data.source.postadoption.StatusLocalDataSource
import com.projects.hanoipetadoption.data.source.postadoption.StatusRemoteDataSource
import com.projects.hanoipetadoption.domain.repository.postadoption.StatusRepository
import java.io.File
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of StatusRepository
 */
class StatusRepositoryImpl(
    private val remoteDataSource: StatusRemoteDataSource,
    private val localDataSource: StatusLocalDataSource
) : StatusRepository {
    override suspend fun getPetStatusUpdates(petId: String): Result<List<PetStatusUpdate>> =
        withContext(Dispatchers.IO) {
            try {
                // Fetch from local data source, page 1, effectively all items for this pet as a fallback
                val cachedUpdates = localDataSource.getPetStatusUpdatesForPet(
                    petId,
                    page = 0,
                    pageSize = Int.MAX_VALUE
                )
                if (cachedUpdates.isNotEmpty()) {
                    // cachedUpdates is already List<PetStatusUpdate>
                    Result.success(cachedUpdates)
                } else {
                    Result.success(emptyList())
                }
            } catch (e: Exception) {
                Result.failure(e) // prefer original exception
            }
        }



    override suspend fun addStatusUpdate(
        update: PetStatusUpdate,
        images: List<File>?
    ): Result<PetStatusUpdate> = withContext(Dispatchers.IO) {
        return@withContext try {
            val createdUpdateResponse = remoteDataSource.addStatusUpdate(update, images)
            // Assuming createdUpdateResponse is the successfully created PetStatusUpdate object from remote.
            // Now save it to the local data source.
            // The localDataSource.savePetStatusUpdate also handles file saving if imageFiles are provided and update.imageUrls needs to be populated.
            // If createdUpdateResponse from remote already has final imageUrls, then imageFiles might not be needed for the local save,
            // but it's safer to pass them if they were the source.
            val locallySavedUpdate = localDataSource.savePetStatusUpdate(createdUpdateResponse, images)
            Result.success(locallySavedUpdate)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteStatusUpdate(updateId: Long): Result<Boolean> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                remoteDataSource.deleteStatusUpdate(updateId) // Assuming this returns success/failure or throws
                // If remote deletion is successful, delete from local data source as well
                localDataSource.deletePetStatusUpdateById(updateId)
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
