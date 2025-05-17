package com.projects.hanoipetadoption.data.source.postadoption

import android.os.Build
import androidx.annotation.RequiresApi
import com.projects.hanoipetadoption.data.model.postadoption.CommentCreate
import com.projects.hanoipetadoption.data.model.postadoption.CommentResponse
import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import com.projects.hanoipetadoption.data.model.postadoption.StatusMediaResponse
import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdateCreate
import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdateListResponse
import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdateResponse
import com.projects.hanoipetadoption.data.network.PostAdoptionApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Date

/**
 * Implementation of StatusRemoteDataSource that uses PostAdoptionApiService
 * 
 * Note: This implementation uses sample data for development since the API
 * endpoints for status updates aren't implemented in the current API service.
 */
class StatusRemoteDataSourceImpl(
    private val apiService: PostAdoptionApiService
) : StatusRemoteDataSource {
    
    // Sample data for development and testing
    private val sampleStatusUpdates = mutableMapOf<Long, StatusUpdateResponse>()
    private val sampleStatusMedia = mutableMapOf<Long, MutableList<StatusMediaResponse>>()
    private val sampleComments = mutableMapOf<Long, MutableList<CommentResponse>>()
    private var lastStatusId = 1000L
    private var lastMediaId = 2000L
    private var lastCommentId = 3000
    
    init {
        // Add some sample data
        createSampleData()
    }
    
    override suspend fun getPetStatusUpdates(
        petId: String,
        page: Int,
        pageSize: Int
    ): StatusUpdateListResponse {
        // In a real implementation, this would call the API
        val updates = sampleStatusUpdates.values
            .filter { it.petId == petId }
            .sortedByDescending { it.updateDate }
        
        val totalCount = updates.size
        val totalPages = (totalCount + pageSize - 1) / pageSize
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, totalCount)
        
        val pagedUpdates = if (startIndex < totalCount) {
            updates.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
        
        // For each update, add its media and comments
        val updatesWithMedia = pagedUpdates.map { update ->
            update.copy(
                media = sampleStatusMedia[update.id] ?: emptyList(),
                comments = sampleComments[update.id] ?: emptyList()
            )
        }
        
        return StatusUpdateListResponse(
            updates = updatesWithMedia,
            total = totalCount,
            page = page,
            pageSize = pageSize,
            totalPages = totalPages
        )
    }
    
    override suspend fun createStatusUpdate(update: StatusUpdateCreate): StatusUpdateResponse {
        // In a real implementation, this would call the API
        val now = java.time.Instant.now().toString()
        val newStatusId = ++lastStatusId
        
        val newUpdate = StatusUpdateResponse(
            id = newStatusId,
            petId = update.petId,
            userId = 1, // Default user ID for sample data
            userName = "Pet Owner",
            content = update.content,
            updateDate = now,
            createdAt = now,
            updatedAt = null,
            media = emptyList(),
            comments = emptyList()
        )
        
        sampleStatusUpdates[newStatusId] = newUpdate
        return newUpdate
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addMediaToStatusUpdate(
        updateId: Long,
        file: File,
        mediaType: String
    ): StatusMediaResponse {
        // In a real implementation, this would use multipart upload with the API
        val now = java.time.Instant.now().toString()
        val newMediaId = ++lastMediaId
        
        val newMedia = StatusMediaResponse(
            id = newMediaId,
            statusUpdateId = updateId,
            mediaType = mediaType,
            filePath = "samples/media/$newMediaId${getExtensionFromMediaType(mediaType)}",
            uploadDate = now,
            createdAt = now,
            updatedAt = null
        )
        
        if (!sampleStatusMedia.containsKey(updateId)) {
            sampleStatusMedia[updateId] = mutableListOf()
        }
        sampleStatusMedia[updateId]?.add(newMedia)
        
        return newMedia
    }
    
    override suspend fun getMediaForStatusUpdate(updateId: Long): List<StatusMediaResponse> {
        // In a real implementation, this would call the API
        return sampleStatusMedia[updateId] ?: emptyList()
    }
    
    override suspend fun deleteStatusUpdate(updateId: Long) {
        // In a real implementation, this would call the API
        sampleStatusUpdates.remove(updateId)
        sampleStatusMedia.remove(updateId)
        sampleComments.remove(updateId)
    }
    
    override suspend fun getStatusUpdateComments(updateId: Long): List<CommentResponse> {
        // In a real implementation, this would call the API
        return sampleComments[updateId] ?: emptyList()
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addCommentToUpdate(
        updateId: Long,
        comment: CommentCreate
    ): CommentResponse {
        // In a real implementation, this would call the API
        val now = java.time.Instant.now().toString()
        val newCommentId = ++lastCommentId
        
        val newComment = CommentResponse(
            id = newCommentId,
            statusUpdateId = updateId,
            userId = 1, // Default user ID for sample data
            userName = "Pet Owner",
            content = comment.content,
            commentDate = now,
            createdAt = now,
            updatedAt = null
        )
        
        if (!sampleComments.containsKey(updateId)) {
            sampleComments[updateId] = mutableListOf()
        }
        sampleComments[updateId]?.add(newComment)
        
        return newComment
    }
    
    override suspend fun deleteComment(commentId: Int) {
        // In a real implementation, this would call the API
        sampleComments.forEach { (_, comments) ->
            comments.removeIf { it.id == commentId }
        }
    }
    
    // Helper method to create sample data
    private fun createSampleData() {
        val now = java.time.Instant.now().toString()
        val yesterday = java.time.Instant.now().minusSeconds(86400).toString() // 1 day ago
        
        // Create sample status updates
        val status1 = StatusUpdateResponse(
            id = 1001,
            petId = "",
            userId = 1,
            userName = "Pet Owner",
            content = "Max is adapting so well to his new home! He loves his new bed.",
            updateDate = now,
            createdAt = now,
            updatedAt = null,
            media = emptyList(),
            comments = emptyList()
        )
        
        val status2 = StatusUpdateResponse(
            id = 1002,
            petId = "",
            userId = 1,
            userName = "Pet Owner",
            content = "First visit to the park today. Max made some new friends!",
            updateDate = yesterday,
            createdAt = yesterday,
            updatedAt = null,
            media = emptyList(),
            comments = emptyList()
        )
        
        // Add to sample data
        sampleStatusUpdates[1001] = status1
        sampleStatusUpdates[1002] = status2
        
        // Create sample media
        val media1 = StatusMediaResponse(
            id = 2001,
            statusUpdateId = 1001,
            mediaType = "image",
            filePath = "samples/media/new_bed.jpg",
            uploadDate = now,
            createdAt = now,
            updatedAt = null
        )
        
        val media2 = StatusMediaResponse(
            id = 2002,
            statusUpdateId = 1002,
            mediaType = "image",
            filePath = "samples/media/park_visit.jpg",
            uploadDate = yesterday,
            createdAt = yesterday,
            updatedAt = null
        )
        
        // Add to sample data
        sampleStatusMedia[1001] = mutableListOf(media1)
        sampleStatusMedia[1002] = mutableListOf(media2)
        
        // Create sample comments
        val comment1 = CommentResponse(
            id = 3001,
            statusUpdateId = 1002,
            userId = 2,
            userName = "Jane Vet",
            content = "Great to see Max enjoying his new home!",
            commentDate = yesterday,
            createdAt = yesterday,
            updatedAt = null
        )
        
        // Add to sample data
        sampleComments[1002] = mutableListOf(comment1)
    }
      // Helper method to get file extension from media type
    private fun getExtensionFromMediaType(mediaType: String): String {
        return when (mediaType.lowercase()) {
            "image" -> ".jpg"
            "video" -> ".mp4"
            "document" -> ".pdf"
            else -> ".dat"
        }
    }
    
    /**
     * Implementation of addStatusUpdate for PetStatusUpdate objects
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addStatusUpdate(update: PetStatusUpdate, images: List<File>?): PetStatusUpdate {
        // Convert PetStatusUpdate to StatusUpdateCreate
        val statusUpdateCreate = StatusUpdateCreate(
            petId = update.petId,
            content = update.description
        )
        
        // Create the status update
        val createdUpdate = createStatusUpdate(statusUpdateCreate)
        
        // Add images if provided
        val mediaList = mutableListOf<StatusMediaResponse>()
        if (!images.isNullOrEmpty() && createdUpdate.id != null) {
            images.forEach { file ->
                val mediaType = when {
                    file.name.endsWith(".jpg", ignoreCase = true) || 
                    file.name.endsWith(".jpeg", ignoreCase = true) || 
                    file.name.endsWith(".png", ignoreCase = true) -> "image"
                    file.name.endsWith(".mp4", ignoreCase = true) -> "video"
                    else -> "document"
                }
                val media = addMediaToStatusUpdate(createdUpdate.id, file, mediaType)
                mediaList.add(media)
            }
        }
        
        // Convert back to PetStatusUpdate
        return PetStatusUpdate(
            id = createdUpdate.id,
            petId = createdUpdate.petId,
            userId = createdUpdate.userId,
            description = createdUpdate.content,
            imageUrls = mediaList.map { it.filePath },
            createdAt = Date() // Use current date as createdAt is a string in StatusUpdateResponse
        )
    }
}
