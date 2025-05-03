package com.projects.hanoipetadoption.domain.usecase

import com.projects.hanoipetadoption.domain.repository.PetRepository

/**
 * Use case to toggle favorite status of a pet
 */
class TogglePetFavoriteUseCase constructor(
    private val petRepository: PetRepository
) {
    suspend operator fun invoke(petId: String): Result<Boolean> {
        return petRepository.toggleFavorite(petId)
    }
}
