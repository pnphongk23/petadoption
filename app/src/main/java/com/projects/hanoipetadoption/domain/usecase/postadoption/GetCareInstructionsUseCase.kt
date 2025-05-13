package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.domain.repository.postadoption.CareRepository

/**
 * Use case to get care instructions for a pet
 */
class GetCareInstructionsUseCase(private val repository: CareRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(petId: Int): Result<List<CareInstructions>> {
        return repository.getCareInstructionsForPet(petId)
    }
}
