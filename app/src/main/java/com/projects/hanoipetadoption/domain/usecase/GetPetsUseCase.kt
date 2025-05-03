package com.projects.hanoipetadoption.domain.usecase

import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.domain.repository.PetRepository

/**
 * Use case for getting all pets
 */
class GetPetsUseCase(private val repository: PetRepository) {
    
    /**
     * Gets all pets
     * @return Result containing a list of pets if successful, or an exception if not
     */
    suspend operator fun invoke(): Result<List<PetDomain>> {
        return repository.getAllPets()
    }
}
