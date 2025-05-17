package com.projects.hanoipetadoption.data.source.postadoption

import android.content.Context
import com.projects.hanoipetadoption.data.local.dao.PetStatusUpdateDao
import com.projects.hanoipetadoption.data.mapper.toEntity
import com.projects.hanoipetadoption.data.mapper.toImageEntity
import com.projects.hanoipetadoption.data.mapper.toModel
import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream

// Class Javadoc: Implementation of StatusLocalDataSource using Room and local file storage.
class StatusLocalDataSourceImpl constructor(
    private val context: Context, // Keep context for file operations
    private val statusUpdateDao: PetStatusUpdateDao
) : StatusLocalDataSource {

    // Cache directory for status update media files, adapted from original
    private val statusMediaDir: File by lazy {
        File(context.filesDir, "status_media").apply {
            if (!exists()) mkdirs()
        }
    }

    override suspend fun savePetStatusUpdates(updates: List<PetStatusUpdate>) {
        // This will insert or replace.
        // If imageFiles are part of these updates (e.g. fetched from remote and URLs are paths to new files to be cached)
        // then more complex logic is needed here or in a sync worker.
        // For now, assumes update.imageUrls are final or savePetStatusUpdate is called individually.
        updates.forEach { savePetStatusUpdate(it, null) }
    }

    override suspend fun getPetStatusUpdatesForPet(petId: String, page: Int, pageSize: Int): List<PetStatusUpdate> {
        // TODO: Implement actual pagination if DAO doesn't support it directly or if full list is too large.
        // Current DAO fetches all for a pet, then maps.
        return statusUpdateDao.getPetStatusUpdatesWithImagesForPet(petId)
            .map { list -> list.map { it.toModel() } }
            .firstOrNull() ?: emptyList()
    }

    override suspend fun getPetStatusUpdateById(updateId: Long): PetStatusUpdate? {
        return statusUpdateDao.getPetStatusUpdateWithImagesById(updateId)?.toModel()
    }

    /**
     * Saves a PetStatusUpdate to the database and its associated images to local file storage.
     *
     * @param update The [PetStatusUpdate] object to save. If its `id` is null, a new entry is created.
     *               Otherwise, the existing entry is updated.
     * @param imageFiles A list of [File] objects representing new images to be saved and associated
     *                   with this status update. If provided, these will replace any existing images.
     *                   If null, existing images (referenced by `update.imageUrls`) are attempted to be preserved
     *                   (though current logic might re-save them if `update.imageUrls` point to temp files not yet in `statusMediaDir`).
     *                   If an empty list, all existing images for this update will be removed.
     * @return The saved [PetStatusUpdate] object, with its `id` populated (if new) and `imageUrls`
     *         updated to reflect the paths of the saved files in local storage.
     */
    override suspend fun savePetStatusUpdate(update: PetStatusUpdate, imageFiles: List<File>?): PetStatusUpdate {
        val entity = update.toEntity() // if update.id is null, entity.id will be 0L for Room to auto-generate
        val savedEntityId = statusUpdateDao.insertPetStatusUpdate(entity) // This is the actual ID from DB

        val finalImageUrls = mutableListOf<String>()

        // Determine the ID to use for directory creation and image association
        // If it was an insert, savedEntityId is the new ID. If an update, update.id should be non-null.
        val effectiveId = update.id ?: savedEntityId

        // Clear old images if new files are provided OR if imageFiles is an empty list (explicit clear)
        if (imageFiles != null) { // This means new files OR an explicit empty list to clear images
            statusUpdateDao.deleteAllImagesForUpdate(effectiveId)
        }

        if (imageFiles != null && imageFiles.isNotEmpty()) {
            val updateSpecificMediaDir = File(statusMediaDir, effectiveId.toString()).apply {
                if (!exists()) mkdirs()
            }

            imageFiles.forEachIndexed { index, file ->
                val fileName = "media_${System.currentTimeMillis()}_${index}.${file.extension}"
                val destinationFile = File(updateSpecificMediaDir, fileName)

                try {
                    file.inputStream().use { input ->
                        FileOutputStream(destinationFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    val newImagePath = destinationFile.absolutePath
                    statusUpdateDao.insertPetStatusUpdateImage(newImagePath.toImageEntity(effectiveId))
                    finalImageUrls.add(newImagePath)
                } catch (e: Exception) {
                    // TODO: Handle file saving exception (e.g., log it, inform user)
                    // Optionally, add placeholder or skip this image
                    println("Error saving file ${file.name}: ${e.message}")
                }
            }
        } else if (imageFiles == null && update.id != null) {
            // No new files provided for an existing update: preserve existing imageUrls from the input 'update' object.
            // These URLs should ideally already point to files within statusMediaDir or be resolvable.
            // If they point to temporary cache files that need to be moved, that logic is missing here.
            update.imageUrls?.let { urls -> finalImageUrls.addAll(urls) }
        } else if (update.id == null && imageFiles == null && update.imageUrls != null) {
             // New item, no new files, but pre-existing URLs in update.imageUrls (e.g. from a draft)
             // These should be treated as files to be saved if they are not already in permanent storage.
             // This case needs careful handling similar to imageFiles processing if URLs are temporary.
             // For now, just copy them over assuming they are already "permanent" in some sense.
            update.imageUrls?.let { finalImageUrls.addAll(it) }
        }

        // Fetch the updated record from DB to ensure consistency and get all relational data
        return statusUpdateDao.getPetStatusUpdateWithImagesById(effectiveId)?.toModel()
            ?: update.copy(id = effectiveId, imageUrls = finalImageUrls) // Fallback, should ideally not happen if DB ops are successful
    }

    override suspend fun deletePetStatusUpdateById(updateId: Long) {
        statusUpdateDao.deletePetStatusUpdateById(updateId)
        // Images are cascade deleted by Room's ForeignKey constraint.
        // Also delete media files directory for this update
        val mediaDirForUpdate = File(statusMediaDir, updateId.toString())
        if (mediaDirForUpdate.exists()) {
            mediaDirForUpdate.deleteRecursively()
        }
    }

    override suspend fun clearOutdatedPetStatusUpdates(olderThan: Long) {
        // TODO: Implement this. Requires a DAO method to get IDs of outdated updates first,
        // then loop through them to delete files and DB entries.
        // Example: val outdatedUpdates = statusUpdateDao.getOutdatedUpdateIds(olderThan)
        // outdatedUpdates.forEach { deletePetStatusUpdateById(it) }
        throw NotImplementedError("clearOutdatedPetStatusUpdates not implemented yet.")
    }
}
