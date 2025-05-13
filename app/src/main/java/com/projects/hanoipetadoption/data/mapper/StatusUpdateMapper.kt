package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdate

/**
 * Mapper functions for converting between StatusUpdate and PetStatusUpdate
 */

/**
 * Convert StatusUpdate to PetStatusUpdate
 */
fun StatusUpdate.toPetStatusUpdate(): PetStatusUpdate {
    return PetStatusUpdate(
        id = this.id,
        petId = this.petId,
        userId = this.userId,
        description = this.content,
        imageUrls = this.mediaItems.filter { it.mediaType == "image" }.map { it.filePath },
        createdAt = this.updateDate
    )
}

/**
 * Convert a list of StatusUpdate to a list of PetStatusUpdate
 */
fun List<StatusUpdate>.toPetStatusUpdateList(): List<PetStatusUpdate> {
    return this.map { it.toPetStatusUpdate() }
}
