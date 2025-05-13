package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import com.projects.hanoipetadoption.domain.repository.postadoption.StatusRepository
import java.io.File

/**
 * Use case to add a status update for a pet
 */
class AddPetStatusUpdateUseCase(private val repository: StatusRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(update: PetStatusUpdate, images: List<File>? = null): Result<PetStatusUpdate> {
        return repository.addStatusUpdate(update, images)
    }
}
