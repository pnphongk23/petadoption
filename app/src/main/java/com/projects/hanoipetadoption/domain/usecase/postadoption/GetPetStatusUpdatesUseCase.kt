package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import com.projects.hanoipetadoption.domain.repository.postadoption.StatusRepository

/**
 * Use case to get status updates for a pet
 */
class GetPetStatusUpdatesUseCase(private val repository: StatusRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(petId: Int): Result<List<PetStatusUpdate>> {
        return repository.getPetStatusUpdates(petId)
    }
}
