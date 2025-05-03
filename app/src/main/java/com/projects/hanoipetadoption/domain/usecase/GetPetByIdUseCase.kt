package com.projects.hanoipetadoption.domain.usecase

import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.domain.repository.PetRepository

/**
 * Use case to get a pet by its ID
 */
class GetPetByIdUseCase (
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(petId: String): Result<PetDomain> {
        return petRepository.getPetById(petId)
    }
}
