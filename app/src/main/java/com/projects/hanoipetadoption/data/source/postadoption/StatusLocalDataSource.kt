package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import java.io.File

/**
 * Interface for local data source operations related to pet status updates (đã refactor model)
 */
interface StatusLocalDataSource {
    
    /**
     * Save/Update a list of status updates to local storage.
     * @param updates List of status updates to save
     */
    suspend fun savePetStatusUpdates(updates: List<PetStatusUpdate>)
    
    /**
     * Get status updates for a pet from local storage
     * @param petId The ID of the pet
     * @param page Pagination page number (0-based)
     * @param pageSize Number of items per page
     * @return List of status updates
     */
    suspend fun getPetStatusUpdatesForPet(
        petId: String,
        page: Int = 0,
        pageSize: Int = 20
    ): List<PetStatusUpdate>
    
    /**
     * Get a status update by ID from local storage
     * @param updateId The ID of the status update
     * @return The status update or null if not found
     */
    suspend fun getPetStatusUpdateById(updateId: Long): PetStatusUpdate?
    
    /**
     * Save/Update a status update to local storage.
     * Handles saving of associated image files by their paths to PetStatusUpdate.imageUrls.
     * @param update The status update to save
     * @param imageFiles Optional list of new image files to associate and save.
     *                   If null, existing images are preserved (not implemented here, assumes full replacement or no change).
     *                   If empty list, existing images might be cleared (logic to be defined in Impl).
     * @return The saved status update with updated imageUrls (paths to saved files).
     */
    suspend fun savePetStatusUpdate(update: PetStatusUpdate, imageFiles: List<File>? = null): PetStatusUpdate
    
    /**
     * Delete a status update from local storage
     * @param updateId The ID of the status update to delete
     */
    suspend fun deletePetStatusUpdateById(updateId: Long)
    
    /**
     * Clear outdated status updates
     * @param olderThan Timestamp in milliseconds; updates older than this will be cleared
     */
    suspend fun clearOutdatedPetStatusUpdates(olderThan: Long)
}
