package com.projects.hanoipetadoption.data.repository

import com.projects.hanoipetadoption.data.dao.AdoptionDao
import com.projects.hanoipetadoption.data.mapper.toDomain
import com.projects.hanoipetadoption.data.model.AdoptionEntity
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.ui.model.AdoptionApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AdoptionRepository(private val adoptionDao: AdoptionDao) { // Removed @Inject

    suspend fun submitApplication(application: AdoptionApplication) {
        val entity = AdoptionEntity(
            petId = application.petId,
            petName = application.petName,
            applicantName = application.applicantName,
            phoneNumber = application.phoneNumber,
            email = application.email,
            address = application.address,
            petExperience = application.petExperience,
            livingSpace = application.livingSpace,
            hoursAtHome = application.hoursAtHome,
        )
        adoptionDao.insertApplication(entity)
    }

    fun getApprovedApplications(): Flow<List<AdoptionEntity>> = adoptionDao.getApprovedApplications()

    fun getAdoptedPetIds(): Flow<List<String>> = adoptionDao.getAdoptedPetIds()
    
    /**
     * Gets all adopted pets by the current user with their details
     * @return Flow of list of PetDomain objects
     */
    fun getAdoptedPets(): Flow<List<PetDomain>> {
        return adoptionDao.getAdoptedPets().map { adoptedPets ->
            adoptedPets.map { adoptedPet -> 
                // Convert PetEntity to PetDomain
                with(adoptedPet.pet) {
                    PetDomain(
                        id = id,
                        name = name,
                        breed = breed,
                        gender = when (gender) {
                            "MALE" -> com.projects.hanoipetadoption.domain.model.GenderDomain.MALE
                            "FEMALE" -> com.projects.hanoipetadoption.domain.model.GenderDomain.FEMALE
                            else -> com.projects.hanoipetadoption.domain.model.GenderDomain.UNKNOWN
                        },
                        age = age,
                        weight = weight ?: "Unknown",
                        characteristics = listOf(),
                        description = description,
                        healthStatus = mapOf(),
                        adoptionRequirements = listOf(),
                        imageRes = imageRes,
                        isFavorite = isFavorite
                    )
                }
            }
        }
    }
}