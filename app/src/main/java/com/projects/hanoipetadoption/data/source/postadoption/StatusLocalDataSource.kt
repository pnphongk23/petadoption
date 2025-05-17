package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdate
import com.projects.hanoipetadoption.data.model.postadoption.StatusMedia
import com.projects.hanoipetadoption.data.model.postadoption.Comment
import java.io.File

/**
 * Interface for local data source operations related to pet status updates
 */
interface StatusLocalDataSource {
    
    /**
     * Save status updates to local storage
     * @param updates List of status updates to save
     */
    suspend fun saveStatusUpdates(updates: List<StatusUpdate>)
    
    /**
     * Get status updates for a pet from local storage
     * @param petId The ID of the pet
     * @param page Pagination page number (0-based)
     * @param pageSize Number of items per page
     * @return List of status updates
     */
    suspend fun getStatusUpdatesForPet(
        petId: String,
        page: Int = 0,
        pageSize: Int = 20
    ): List<StatusUpdate>
    
    /**
     * Get cached status updates for a pet
     * @param petId The ID of the pet
     * @return List of cached status updates
     */
    suspend fun getCachedPetStatusUpdates(petId: String): List<StatusUpdate>
    
    /**
     * Get a status update by ID from local storage
     * @param updateId The ID of the status update
     * @return The status update or null if not found
     */
    suspend fun getStatusUpdate(updateId: Int): StatusUpdate?
    
    /**
     * Save a status update to local storage
     * @param update The status update to save
     */
    suspend fun saveStatusUpdate(update: StatusUpdate)
    
    /**
     * Save media items for a status update to local storage
     * @param updateId The ID of the status update
     * @param media List of media items to save
     */
    suspend fun saveMediaForStatusUpdate(updateId: Int, media: List<StatusMedia>)
    
    /**
     * Get media items for a status update from local storage
     * @param updateId The ID of the status update
     * @return List of media items
     */
    suspend fun getMediaForStatusUpdate(updateId: Int): List<StatusMedia>
    
    /**
     * Save a status update with media to local storage
     * @param update The status update
     * @param mediaFiles List of media files
     * @return The saved status update
     */
    suspend fun saveStatusUpdateWithMedia(
        update: StatusUpdate,
        mediaFiles: List<File> = emptyList()
    ): StatusUpdate
    
    /**
     * Delete a status update from local storage
     * @param updateId The ID of the status update to delete
     */
    suspend fun deleteStatusUpdate(updateId: Int)
    
    /**
     * Save comments for a status update to local storage
     * @param updateId The ID of the status update
     * @param comments List of comments to save
     */
    suspend fun saveComments(updateId: Int, comments: List<Comment>)
    
    /**
     * Get comments for a status update from local storage
     * @param updateId The ID of the status update
     * @return List of comments
     */
    suspend fun getCommentsForUpdate(updateId: Int): List<Comment>
    
    /**
     * Save a comment to local storage
     * @param comment The comment to save
     */
    suspend fun saveComment(comment: Comment)
    
    /**
     * Delete a comment from local storage
     * @param commentId The ID of the comment to delete
     */
    suspend fun deleteComment(commentId: Int)
    
    /**
     * Clear outdated status updates
     * @param olderThan Timestamp in milliseconds; updates older than this will be cleared
     */
    suspend fun clearOutdatedStatusUpdates(olderThan: Long)
}
