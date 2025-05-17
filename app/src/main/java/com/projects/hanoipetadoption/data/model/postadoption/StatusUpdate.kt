package com.projects.hanoipetadoption.data.model.postadoption

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Domain model for pet status update
 */
data class StatusUpdate(
    val id: Long? = null,
    val petId: String,
    val userId: Int? = null,
    val content: String,
    val updateDate: Date = Date(),
    val mediaItems: List<StatusMedia> = emptyList(),
    val comments: List<Comment> = emptyList()
)

/**
 * Domain model for status update media
 */
data class StatusMedia(
    val id: Long? = null,
    val statusUpdateId: Long? = null,
    val mediaType: String,
    val filePath: String,
    val uploadDate: Date = Date()
)

/**
 * Domain model for comment
 */
data class Comment(
    val id: Int? = null,
    val statusUpdateId: Int,
    val userId: Int? = null,
    val userName: String? = null,
    val content: String,
    val commentDate: Date = Date()
)

/**
 * API response model for status update
 */
data class StatusUpdateResponse(
    val id: Long,
    @SerializedName("pet_id")
    val petId: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_name")
    val userName: String,
    val content: String,
    @SerializedName("update_date")
    val updateDate: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val media: List<StatusMediaResponse>?,
    val comments: List<CommentResponse>?
)

/**
 * Paginated response for status updates
 */
data class StatusUpdateListResponse(
    val updates: List<StatusUpdateResponse>,
    val total: Int,
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

/**
 * API response model for status media
 */
data class StatusMediaResponse(
    val id: Long,
    @SerializedName("status_update_id")
    val statusUpdateId: Long,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("file_path")
    val filePath: String,
    @SerializedName("upload_date")
    val uploadDate: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String?
)

/**
 * API response model for comment
 */
data class CommentResponse(
    val id: Int,
    @SerializedName("status_update_id")
    val statusUpdateId: Long,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_name")
    val userName: String,
    val content: String,
    @SerializedName("comment_date")
    val commentDate: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String?
)

/**
 * Request model for creating status update
 */
data class StatusUpdateCreate(
    @SerializedName("pet_id")
    val petId: String,
    val content: String
)

/**
 * Request model for creating comment
 */
data class CommentCreate(
    val content: String
)
