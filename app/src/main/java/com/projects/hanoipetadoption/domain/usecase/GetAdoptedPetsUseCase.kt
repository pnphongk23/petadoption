package com.projects.hanoipetadoption.domain.usecase

import com.projects.hanoipetadoption.data.repository.AdoptionRepository
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

/**
 * Use case to get a list of all pets adopted by the current user
 */
class GetAdoptedPetsUseCase(
    private val adoptionRepository: AdoptionRepository,
    private val petRepository: PetRepository
) {
    /**
     * Get a flow of all adopted pets
     * @return Flow of Result containing a list of PetDomain objects or an error
     */
    operator fun invoke(): Flow<Result<List<PetDomain>>> {
        return adoptionRepository.getAdoptedPetIds().flatMapLatest { petIds ->
            flow {
                val adoptedPets = mutableListOf<PetDomain>()
                var hasError = false
                var errorMessage = ""
                
                for (id in petIds) {
                    val petResult = petRepository.getPetById(id)
                    
                    petResult.fold(
                        onSuccess = { pet ->
                            adoptedPets.add(pet)
                        },
                        onFailure = { error ->
                            hasError = true
                            errorMessage = error.message ?: "Unknown error fetching pet $id"
                        }
                    )
                }
                
                if (hasError && adoptedPets.isEmpty()) {
                    emit(Result.failure<List<PetDomain>>(Exception(errorMessage)))
                } else {
                    emit(Result.success(adoptedPets))
                }
            }
        }
    }
} 