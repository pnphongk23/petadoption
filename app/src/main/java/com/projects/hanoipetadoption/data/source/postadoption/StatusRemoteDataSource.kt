package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdateResponse
import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdateCreate
import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdateListResponse
import com.projects.hanoipetadoption.data.model.postadoption.StatusMediaResponse
import com.projects.hanoipetadoption.data.model.postadoption.CommentResponse
import com.projects.hanoipetadoption.data.model.postadoption.CommentCreate
import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import java.io.File

/**
 * Interface for remote data source operations related to pet status updates
 */
interface StatusRemoteDataSource {
    
    /**
     * Get status updates for a pet
     * @param petId The ID of the pet
     * @param page Pagination page number (0-based)
     * @param pageSize Number of items per page
     * @return Paginated list of status updates
     */
    suspend fun getPetStatusUpdates(
        petId: String,
        page: Int = 0,
        pageSize: Int = 20
    ): StatusUpdateListResponse
    
    /**
     * Add a status update with optional images
     * @param update The status update data
     * @param images Optional list of image files to attach
     * @return The created status update
     */
    suspend fun addStatusUpdate(update: PetStatusUpdate, images: List<File>? = null): PetStatusUpdate
    
    /**
     * Create a new status update
     * @param update The status update data
     * @return The created status update
     */
    suspend fun createStatusUpdate(update: StatusUpdateCreate): StatusUpdateResponse
    
    /**
     * Add media to a status update
     * @param updateId The ID of the status update
     * @param file The media file
     * @param mediaType The type of media (image, video, etc.)
     * @return The created media response
     */
    suspend fun addMediaToStatusUpdate(
        updateId: Int,
        file: File,
        mediaType: String
    ): StatusMediaResponse
    
    /**
     * Get media for a status update
     * @param updateId The ID of the status update
     * @return List of media items
     */
    suspend fun getMediaForStatusUpdate(updateId: Int): List<StatusMediaResponse>
    
    /**
     * Delete a status update
     * @param updateId The ID of the status update to delete
     */
    suspend fun deleteStatusUpdate(updateId: Int)
    
    /**
     * Get comments for a status update
     * @param updateId The ID of the status update
     * @return List of comments
     */
    suspend fun getStatusUpdateComments(updateId: Int): List<CommentResponse>
    
    /**
     * Add a comment to a status update
     * @param updateId The ID of the status update
     * @param comment The comment data
     * @return The created comment
     */
    suspend fun addCommentToUpdate(
        updateId: Int,
        comment: CommentCreate
    ): CommentResponse
    
    /**
     * Delete a comment
     * @param commentId The ID of the comment to delete
     */
    suspend fun deleteComment(commentId: Int)
}
