package com.projects.hanoipetadoption.domain.repository.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import java.io.File

/**
 * Repository interface for pet status updates
 */
interface StatusRepository {
    /**
     * Get status updates for a specific pet
     * 
     * @param petId ID of the pet
     * @return List of status updates
     */
    suspend fun getPetStatusUpdates(petId: String): Result<List<PetStatusUpdate>>

    /**
     * Add a new status update
     * 
     * @param update The status update to add
     * @param images Optional list of image files to attach
     * @return The created status update
     */
    suspend fun addStatusUpdate(update: PetStatusUpdate, images: List<File>? = null): Result<PetStatusUpdate>

    /**
     * Delete a status update
     * 
     * @param updateId ID of the status update to delete
     * @return Whether the deletion was successful
     */
    suspend fun deleteStatusUpdate(updateId: Long): Result<Boolean>
}
