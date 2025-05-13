package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.MediaType
import com.projects.hanoipetadoption.domain.repository.postadoption.HealthRepository
import java.io.File

/**
 * Use case to add media to a health record
 */
class AddMediaToHealthRecordUseCase(private val repository: HealthRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(
        recordId: Int,
        file: File,
        mediaType: MediaType
    ): Result<HealthRecordMedia> {
        return repository.addMediaToHealthRecord(recordId, file, mediaType)
    }
}
