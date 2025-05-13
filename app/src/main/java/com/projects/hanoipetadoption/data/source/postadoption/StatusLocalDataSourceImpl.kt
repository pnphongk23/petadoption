package com.projects.hanoipetadoption.data.source.postadoption

import android.content.Context
import com.projects.hanoipetadoption.data.model.postadoption.Comment
import com.projects.hanoipetadoption.data.model.postadoption.StatusMedia
import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdate
import java.io.File
import java.io.FileOutputStream
import java.util.Date

/**
 * Implementation of StatusLocalDataSource that uses Room database and file system
 */
class StatusLocalDataSourceImpl(
    private val context: Context
) : StatusLocalDataSource {
    
    // Cache directory for status update media files
    private val statusMediaDir: File by lazy {
        File(context.filesDir, "status_media").apply {
            if (!exists()) mkdirs()
        }
    }
    
    // In-memory cache for status updates (temporary until Room implementation)
    private val statusUpdatesCache = mutableMapOf<Int, StatusUpdate>()
    private val mediaCache = mutableMapOf<Int, MutableList<StatusMedia>>()
    private val commentsCache = mutableMapOf<Int, MutableList<Comment>>()
    
    override suspend fun saveStatusUpdates(updates: List<StatusUpdate>) {
        // Add to in-memory cache (temporary)
        updates.forEach { update ->
            update.id?.let { id ->
                statusUpdatesCache[id] = update
                
                // Also cache media and comments
                mediaCache[id] = update.mediaItems.toMutableList()
                commentsCache[id] = update.comments.toMutableList()
            }
        }
        
        // TODO: Implement Room database storage
        // Example:
        // statusUpdateDao.insertAll(updates.map { it.toStatusUpdateEntity() })
    }
    
    override suspend fun getStatusUpdatesForPet(
        petId: Int,
        page: Int,
        pageSize: Int
    ): List<StatusUpdate> {
        // Filter from in-memory cache (temporary)
        val allUpdates = statusUpdatesCache.values
            .filter { it.petId == petId }
            .sortedByDescending { it.updateDate }
        
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, allUpdates.size)
        
        return if (startIndex < allUpdates.size) {
            allUpdates.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
        
        // TODO: Implement Room database query with pagination
        // Example:
        // return statusUpdateDao.getStatusUpdatesForPet(petId, pageSize, page * pageSize)
        //     .map { it.toStatusUpdate() }
    }
    
    override suspend fun getStatusUpdate(updateId: Int): StatusUpdate? {
        // Return from in-memory cache (temporary)
        val update = statusUpdatesCache[updateId] ?: return null
        
        // Get associated media and comments
        val media = mediaCache[updateId] ?: emptyList()
        val comments = commentsCache[updateId] ?: emptyList()
        
        // Return combined data
        return update.copy(
            mediaItems = media,
            comments = comments
        )
        
        // TODO: Implement Room database query with relationships
        // Example:
        // val updateWithRelations = statusUpdateDao.getStatusUpdateWithRelations(updateId)
        // return updateWithRelations?.toStatusUpdate()
    }
    
    override suspend fun saveStatusUpdate(update: StatusUpdate) {
        // Add to in-memory cache (temporary)
        update.id?.let { id ->
            statusUpdatesCache[id] = update
            
            // Also update media and comments cache
            mediaCache[id] = update.mediaItems.toMutableList()
            commentsCache[id] = update.comments.toMutableList()
        }
        
        // TODO: Implement Room database insertion with relationships
        // Example:
        // statusUpdateDao.insertWithRelations(
        //     update.toStatusUpdateEntity(),
        //     update.mediaItems.map { it.toStatusMediaEntity() },
        //     update.comments.map { it.toCommentEntity() }
        // )
    }
    
    override suspend fun saveMediaForStatusUpdate(updateId: Int, media: List<StatusMedia>) {
        // Add to in-memory cache (temporary)
        if (!mediaCache.containsKey(updateId)) {
            mediaCache[updateId] = mutableListOf()
        }
        mediaCache[updateId]?.addAll(media)
        
        // Update the status update object too
        statusUpdatesCache[updateId]?.let { update ->
            val updatedMediaList = update.mediaItems.toMutableList()
            updatedMediaList.addAll(media)
            statusUpdatesCache[updateId] = update.copy(mediaItems = updatedMediaList)
        }
        
        // TODO: Implement Room database insertion
        // Example:
        // statusMediaDao.insertAll(media.map { it.toStatusMediaEntity() })
    }
    
    override suspend fun getMediaForStatusUpdate(updateId: Int): List<StatusMedia> {
        // Return from in-memory cache (temporary)
        return mediaCache[updateId] ?: emptyList()
        
        // TODO: Implement Room database query
        // Example:
        // return statusMediaDao.getMediaForStatusUpdate(updateId)
        //     .map { it.toStatusMedia() }
    }
    
    override suspend fun saveStatusUpdateWithMedia(
        update: StatusUpdate,
        mediaFiles: List<File>
    ): StatusUpdate {
        // First save the status update
        val id = update.id ?: return update
        saveStatusUpdate(update)
        
        // Then save each media file to local storage and create StatusMedia objects
        val savedMedia = mutableListOf<StatusMedia>()
        
        for ((index, file) in mediaFiles.withIndex()) {
            // Create a directory for this update's media if needed
            val updateMediaDir = File(statusMediaDir, id.toString()).apply {
                if (!exists()) mkdirs()
            }
            
            // Determine media type from file extension
            val mediaType = when {
                file.name.endsWith(".jpg", ignoreCase = true) || 
                file.name.endsWith(".jpeg", ignoreCase = true) || 
                file.name.endsWith(".png", ignoreCase = true) -> "image"
                file.name.endsWith(".mp4", ignoreCase = true) || 
                file.name.endsWith(".mov", ignoreCase = true) -> "video"
                else -> "document"
            }
            
            // Create a unique file name
            val fileName = "media_${System.currentTimeMillis()}_$index${file.extension}"
            val destinationFile = File(updateMediaDir, fileName)
            
            // Copy the file
            file.inputStream().use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            // Create a StatusMedia object
            val media = StatusMedia(
                id = (id * 1000) + index, // Temporary ID generation
                statusUpdateId = id,
                mediaType = mediaType,
                filePath = destinationFile.absolutePath,
                uploadDate = Date()
            )
            
            savedMedia.add(media)
        }
        
        // Save the media objects
        if (savedMedia.isNotEmpty()) {
            saveMediaForStatusUpdate(id, savedMedia)
        }
        
        // Return the updated status update with media
        return statusUpdatesCache[id]?.copy(
            mediaItems = mediaCache[id] ?: emptyList()
        ) ?: update
    }
    
    override suspend fun deleteStatusUpdate(updateId: Int) {
        // Remove from in-memory cache (temporary)
        statusUpdatesCache.remove(updateId)
        mediaCache.remove(updateId)
        commentsCache.remove(updateId)
        
        // Also delete media files
        val mediaDir = File(statusMediaDir, updateId.toString())
        if (mediaDir.exists()) {
            mediaDir.deleteRecursively()
        }
        
        // TODO: Implement Room database deletion with relationships
        // Example:
        // statusUpdateDao.deleteStatusUpdateWithRelations(updateId)
    }
    
    override suspend fun saveComments(updateId: Int, comments: List<Comment>) {
        // Add to in-memory cache (temporary)
        if (!commentsCache.containsKey(updateId)) {
            commentsCache[updateId] = mutableListOf()
        }
        commentsCache[updateId]?.addAll(comments)
        
        // Update the status update object too
        statusUpdatesCache[updateId]?.let { update ->
            val updatedCommentList = update.comments.toMutableList()
            updatedCommentList.addAll(comments)
            statusUpdatesCache[updateId] = update.copy(comments = updatedCommentList)
        }
        
        // TODO: Implement Room database insertion
        // Example:
        // commentDao.insertAll(comments.map { it.toCommentEntity() })
    }
    
    override suspend fun getCommentsForUpdate(updateId: Int): List<Comment> {
        // Return from in-memory cache (temporary)
        return commentsCache[updateId] ?: emptyList()
        
        // TODO: Implement Room database query
        // Example:
        // return commentDao.getCommentsForUpdate(updateId)
        //     .map { it.toComment() }
    }
    
    override suspend fun saveComment(comment: Comment) {
        // Add to in-memory cache (temporary)
        val updateId = comment.statusUpdateId
        
        if (!commentsCache.containsKey(updateId)) {
            commentsCache[updateId] = mutableListOf()
        }
        commentsCache[updateId]?.add(comment)
        
        // Update the status update object too
        statusUpdatesCache[updateId]?.let { update ->
            val updatedCommentList = update.comments.toMutableList()
            updatedCommentList.add(comment)
            statusUpdatesCache[updateId] = update.copy(comments = updatedCommentList)
        }
        
        // TODO: Implement Room database insertion
        // Example:
        // commentDao.insert(comment.toCommentEntity())
    }
    
    override suspend fun deleteComment(commentId: Int) {
        // Remove from in-memory cache (temporary)
        commentsCache.forEach { (updateId, comments) ->
            val originalSize = comments.size
            comments.removeIf { it.id == commentId }
            
            // If we actually removed a comment, update the status update too
            if (comments.size < originalSize) {
                statusUpdatesCache[updateId]?.let { update ->
                    statusUpdatesCache[updateId] = update.copy(
                        comments = update.comments.filter { it.id != commentId }
                    )
                }
            }
        }
        
        // TODO: Implement Room database deletion
        // Example:
        // commentDao.deleteComment(commentId)
    }
      override suspend fun clearOutdatedStatusUpdates(olderThan: Long) {
        val cutoffTime = Date(olderThan)
        
        // Filter from in-memory cache (temporary)
        val updatesToRemove = statusUpdatesCache.values
            .filter { it.updateDate.before(cutoffTime) }
            .mapNotNull { it.id }
        
        updatesToRemove.forEach { updateId ->
            statusUpdatesCache.remove(updateId)
            mediaCache.remove(updateId)
            commentsCache.remove(updateId)
            
            // Also delete media files
            val mediaDir = File(statusMediaDir, updateId.toString())
            if (mediaDir.exists()) {
                mediaDir.deleteRecursively()
            }
        }
        
        // TODO: Implement Room database deletion
        // Example:
        // statusUpdateDao.deleteUpdatesOlderThan(cutoffDate)
    }
    
    /**
     * Get cached status updates for a pet from in-memory cache
     */
    override suspend fun getCachedPetStatusUpdates(petId: Int): List<StatusUpdate> {
        // Return from in-memory cache (temporary)
        return statusUpdatesCache.values
            .filter { it.petId == petId }
            .sortedByDescending { it.updateDate }
            .map { update ->
                val id = update.id
                // Get associated media and comments for complete data
                val media = if (id != null) mediaCache[id] ?: emptyList() else emptyList()
                val comments = if (id != null) commentsCache[id] ?: emptyList() else emptyList()
                
                update.copy(
                    mediaItems = media,
                    comments = comments
                )
            }
    }
}
