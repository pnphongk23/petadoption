package com.projects.hanoipetadoption.data.repository

import com.projects.hanoipetadoption.data.dao.AdoptionDao
import com.projects.hanoipetadoption.data.model.AdoptionEntity
import com.projects.hanoipetadoption.ui.model.AdoptionApplication
import kotlinx.coroutines.flow.Flow

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
}