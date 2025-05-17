package com.projects.hanoipetadoption.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projects.hanoipetadoption.data.model.PetHealthStatusEntity

/**
 * DAO for pet health status
 */
@Dao
interface PetHealthStatusDao {
    @Query("SELECT * FROM pet_health_status WHERE petId = :petId")
    suspend fun getHealthStatusForPet(petId: String): List<PetHealthStatusEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthStatus(healthStatus: List<PetHealthStatusEntity>)
    
    @Query("DELETE FROM pet_health_status WHERE petId = :petId")
    suspend fun deleteHealthStatusForPet(petId: String)
} 