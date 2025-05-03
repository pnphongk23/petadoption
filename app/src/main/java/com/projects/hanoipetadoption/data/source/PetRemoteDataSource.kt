// filepath: e:\Git\hanoipetadoption\app\src\main\java\com\projects\hanoipetadoption\data\source\PetRemoteDataSource.kt
package com.projects.hanoipetadoption.data.source

import com.projects.hanoipetadoption.data.model.PetData
import com.projects.hanoipetadoption.data.model.PetListResponse

/**
 * Interface for remote data source operations related to Pets
 */
interface PetRemoteDataSource {
    suspend fun getAllPets(
        skip: Int = 0,
        limit: Int = 100,
        status: String? = null,
        type: String? = null, 
        breed: String? = null,
        gender: String? = null
    ): PetListResponse
    
    suspend fun getPetById(id: String): PetData
    
    suspend fun getPetStatistics(): Map<String, Any>
}
