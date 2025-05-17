package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.local.entity.HealthRecordEntity
import com.projects.hanoipetadoption.data.local.entity.HealthRecordMediaEntity
import com.projects.hanoipetadoption.data.local.relation.HealthRecordWithMedia
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.MediaType as ModelMediaType
import com.projects.hanoipetadoption.data.model.postadoption.RecordType as ModelRecordType

// Mapper for HealthRecordMedia
fun HealthRecordMediaEntity.toModel(): HealthRecordMedia {
    return HealthRecordMedia(
        id = this.id.toInt(), // Be cautious with Long to Int conversion if IDs can exceed Int.MAX_VALUE
        healthRecordId = this.healthRecordId.toInt(),
        mediaType = ModelMediaType.valueOf(this.mediaType.name),
        filePath = this.filePath,
        uploadDate = this.uploadDate
    )
}

fun HealthRecordMedia.toEntity(defaultHealthRecordId: Long): HealthRecordMediaEntity {
    return HealthRecordMediaEntity(
        id = this.id?.toLong() ?: 0L, // Assuming 0L for new unsaved entities
        healthRecordId = this.healthRecordId?.toLong() ?: defaultHealthRecordId,
        mediaType = com.projects.hanoipetadoption.data.model.postadoption.MediaType.valueOf(this.mediaType.name),
        filePath = this.filePath,
        uploadDate = this.uploadDate
    )
}

// Mapper for HealthRecord and HealthRecordWithMedia
fun HealthRecordWithMedia.toModel(): HealthRecord {
    return HealthRecord(
        id = this.healthRecord.id.toInt(),
        petId = this.healthRecord.petId,
        userId = this.healthRecord.userId,
        recordType = ModelRecordType.valueOf(this.healthRecord.recordType.name),
        notes = this.healthRecord.notes,
        weight = this.healthRecord.weight,
        recordDate = this.healthRecord.recordDate,
        nextReminderDate = this.healthRecord.nextReminderDate,
        mediaItems = this.mediaItems.map { it.toModel() }
    )
}

fun HealthRecord.toEntity(): HealthRecordEntity {
    return HealthRecordEntity(
        id = this.id?.toLong() ?: 0L,
        petId = this.petId,
        userId = this.userId,
        recordType = com.projects.hanoipetadoption.data.model.postadoption.RecordType.valueOf(this.recordType.name),
        notes = this.notes,
        weight = this.weight,
        recordDate = this.recordDate,
        nextReminderDate = this.nextReminderDate
        // mediaItems will be handled separately by saving HealthRecordMediaEntity list
    )
}

// Individual entity to model (if needed without full relation)
fun HealthRecordEntity.toModelSimple(): HealthRecord {
    return HealthRecord(
        id = this.id.toInt(),
        petId = this.petId,
        userId = this.userId,
        recordType = ModelRecordType.valueOf(this.recordType.name),
        notes = this.notes,
        weight = this.weight,
        recordDate = this.recordDate,
        nextReminderDate = this.nextReminderDate,
        mediaItems = emptyList() // No media items in this simple mapping
    )
} 