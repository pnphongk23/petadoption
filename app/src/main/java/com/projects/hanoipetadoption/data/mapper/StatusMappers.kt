package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.local.entity.PetStatusUpdateEntity
import com.projects.hanoipetadoption.data.local.entity.PetStatusUpdateImageEntity
import com.projects.hanoipetadoption.data.local.relation.PetStatusUpdateWithImages
import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate // Model đã được cập nhật
// import com.projects.hanoipetadoption.data.model.postadoption.StatusUpdate // If mapping to StatusUpdate model is also needed
// import com.projects.hanoipetadoption.data.model.postadoption.StatusMedia // If mapping to StatusMedia model is also needed

// Mapper for PetStatusUpdateImageEntity to String (URL)
fun PetStatusUpdateImageEntity.toModelUrl(): String {
    return this.imageUrl
}

// Mapper for String (URL) to PetStatusUpdateImageEntity
fun String.toImageEntity(statusUpdateId: Long): PetStatusUpdateImageEntity {
    return PetStatusUpdateImageEntity(
        statusUpdateId = statusUpdateId,
        imageUrl = this
    )
}

// Mapper for PetStatusUpdateWithImages (Room relation) to PetStatusUpdate (Domain/Repository model)
fun PetStatusUpdateWithImages.toModel(): PetStatusUpdate { // Đổi tên toModel cho nhất quán
    return PetStatusUpdate(
        id = this.statusUpdate.id, // id của entity đã là Long, id của model giờ cũng là Long?
        petId = this.statusUpdate.petId,
        userId = this.statusUpdate.userId,
        description = this.statusUpdate.description,
        imageUrls = this.images.map { it.toModelUrl() },
        createdAt = this.statusUpdate.createdAt,
        milestone = this.statusUpdate.milestone
    )
}

// Mapper for PetStatusUpdate (Domain/Repository model) to PetStatusUpdateEntity (Room entity)
fun PetStatusUpdate.toEntity(): PetStatusUpdateEntity {
    return PetStatusUpdateEntity(
        id = this.id ?: 0L, // Nếu id là null (new item), Room sẽ autoGenerate. Nếu không null, dùng id đó.
        petId = this.petId,
        userId = this.userId,
        description = this.description,
        createdAt = this.createdAt,
        milestone = this.milestone
        // imageUrls are handled separately by saving PetStatusUpdateImageEntity list
    )
}

// Mapper for PetStatusUpdateEntity (Room entity) to PetStatusUpdate (Domain/Repository model) - simple mapping without images
fun PetStatusUpdateEntity.toModelSimple(): PetStatusUpdate { // Đổi tên toModelSimple cho nhất quán
    return PetStatusUpdate(
        id = this.id, // id của entity đã là Long, id của model giờ cũng là Long?
        petId = this.petId,
        userId = this.userId,
        description = this.description,
        imageUrls = emptyList(), // No images in this simple mapping
        createdAt = this.createdAt,
        milestone = this.milestone
    )
} 