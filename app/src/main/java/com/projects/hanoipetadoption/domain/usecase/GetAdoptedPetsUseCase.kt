package com.projects.hanoipetadoption.domain.usecase

import com.projects.hanoipetadoption.data.repository.AdoptionRepository
import com.projects.hanoipetadoption.domain.model.PetDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Use case to get a list of all pets adopted by the current user
 */
class GetAdoptedPetsUseCase(
    private val adoptionRepository: AdoptionRepository
) {
    /**
     * Get a flow of all adopted pets
     * @return Flow of Result containing a list of PetDomain objects or an error
     */
    operator fun invoke(): Flow<Result<List<PetDomain>>> {
        return adoptionRepository.getAdoptedPets()
            .map { Result.success(it) }
            .catch { e -> emit(Result.failure(e)) }
    }
} 