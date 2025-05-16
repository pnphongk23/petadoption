package com.projects.hanoipetadoption.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projects.hanoipetadoption.data.model.PetAdoptionRequirementEntity

/**
 * DAO for pet adoption requirements
 */
@Dao
interface PetAdoptionRequirementDao {
    @Query("SELECT * FROM pet_adoption_requirements WHERE petId = :petId")
    suspend fun getRequirementsForPet(petId: String): List<PetAdoptionRequirementEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequirements(requirements: List<PetAdoptionRequirementEntity>)
    
    @Query("DELETE FROM pet_adoption_requirements WHERE petId = :petId")
    suspend fun deleteRequirementsForPet(petId: String)
} 