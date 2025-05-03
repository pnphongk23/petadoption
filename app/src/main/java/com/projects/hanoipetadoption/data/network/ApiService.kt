// filepath: e:\Git\hanoipetadoption\app\src\main\java\com\projects\hanoipetadoption\data\network\ApiService.kt
package com.projects.hanoipetadoption.data.network

import com.projects.hanoipetadoption.data.model.PetListResponse
import com.projects.hanoipetadoption.data.model.PetResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service interface for connecting to the backend
 */
interface ApiService {
    
    @GET("api/pets")
    suspend fun getPets(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100,
        @Query("status") status: String? = null,
        @Query("type") type: String? = null,
        @Query("breed") breed: String? = null,
        @Query("gender") gender: String? = null
    ): PetListResponse
    
    @GET("api/pets/{petId}")
    suspend fun getPetById(@Path("petId") petId: String): PetResponse

    @GET("api/pets/statistics/count")
    suspend fun getPetStatistics(): Map<String, Any>
}
