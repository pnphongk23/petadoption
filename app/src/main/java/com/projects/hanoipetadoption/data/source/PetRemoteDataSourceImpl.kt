// filepath: e:\Git\hanoipetadoption\app\src\main\java\com\projects\hanoipetadoption\data\source\PetRemoteDataSourceImpl.kt
package com.projects.hanoipetadoption.data.source

import com.projects.hanoipetadoption.data.mapper.toPetData
import com.projects.hanoipetadoption.data.model.PetData
import com.projects.hanoipetadoption.data.model.PetListResponse
import com.projects.hanoipetadoption.data.network.ApiService
import com.projects.hanoipetadoption.ui.model.PetGender

/**
 * Implementation of PetRemoteDataSource that uses Retrofit API service
 */
class PetRemoteDataSourceImpl(private val apiService: ApiService) : PetRemoteDataSource {
    
    override suspend fun getAllPets(
        skip: Int, 
        limit: Int, 
        status: String?,
        type: String?,
        breed: String?,
        gender: String?
    ): PetListResponse {
        return apiService.getPets(skip, limit, status, type, breed, gender)
    }
      override suspend fun getPetById(id: String): PetData {
        val response = apiService.getPetById(id)
        return response.toPetData()
    }
    
    override suspend fun getPetStatistics(): Map<String, Any> {
        return apiService.getPetStatistics()
    }
    
    // Helper functions
    private fun calculateAgeDisplay(ageInMonths: Int?): String {
        if (ageInMonths == null) return "Không rõ"
        
        return when {
            ageInMonths < 3 -> "Sơ sinh" 
            ageInMonths < 12 -> "Nhí"
            ageInMonths < 24 -> "Nhỡ"
            else -> "Trưởng thành"
        }
    }
    
    private fun mapGender(gender: String?): PetGender {
        return when (gender?.lowercase()) {
            "male" -> PetGender.MALE
            "female" -> PetGender.FEMALE
            else -> PetGender.MALE // Default if unknown
        }
    }
}
