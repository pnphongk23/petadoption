package com.projects.hanoipetadoption.data.repository.postadoption

import com.projects.hanoipetadoption.data.mapper.toPetStatusUpdate
import com.projects.hanoipetadoption.data.mapper.toPetStatusUpdateList
import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import com.projects.hanoipetadoption.data.model.postadoption.StatusMedia
import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdate
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
) : StatusRepository {    override suspend fun getPetStatusUpdates(petId: Int): Result<List<PetStatusUpdate>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                // Get updates from remote source and convert them to PetStatusUpdate format
                val updateResponse = remoteDataSource.getPetStatusUpdates(petId)
                // Extract just the updates from the response
                val updates = updateResponse.updates.map { response ->
                    PetStatusUpdate(
                        id = response.id,
                        petId = response.petId,
                        userId = response.userId,
                        description = response.content,
                        imageUrls = response.media?.filter { it.mediaType == "image" }?.map { it.filePath },
                        createdAt = Date() // Using current date as the response date format might be different
                    )
                }
                // Save these updates to local cache
                localDataSource.saveStatusUpdates(updates.map { petUpdate ->
                    // Convert PetStatusUpdate back to StatusUpdate for local storage
                    com.projects.hanoipetadoption.data.model.postadoption.StatusUpdate(
                        id = petUpdate.id,
                        petId = petUpdate.petId, 
                        userId = petUpdate.userId,
                        content = petUpdate.description,
                        updateDate = petUpdate.createdAt,
                        mediaItems = petUpdate.imageUrls?.map { url ->
                            com.projects.hanoipetadoption.data.model.postadoption.StatusMedia(
                                id = null,
                                statusUpdateId = petUpdate.id,
                                mediaType = "image",
                                filePath = url,
                                uploadDate = petUpdate.createdAt
                            )
                        } ?: emptyList(),
                        comments = emptyList()
                    )
                })
                Result.success(updates)
            } catch (e: Exception) {
                try {
                    val cachedUpdates = localDataSource.getCachedPetStatusUpdates(petId)
                    if (cachedUpdates.isNotEmpty()) {
                        // Convert StatusUpdate to PetStatusUpdate using mapper
                        Result.success(cachedUpdates.toPetStatusUpdateList())
                    } else {
                        Result.failure(e)
                    }
                } catch (cacheException: Exception) {
                    Result.failure(e)
                }
            }
        }

    override suspend fun addStatusUpdate(
        update: PetStatusUpdate,
        images: List<File>?
    ): Result<PetStatusUpdate> = withContext(Dispatchers.IO) {
        return@withContext try {
            val createdUpdate = remoteDataSource.addStatusUpdate(update, images)
            Result.success(createdUpdate)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteStatusUpdate(updateId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                remoteDataSource.deleteStatusUpdate(updateId)
                // Return success with true since the deletion was successful
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
