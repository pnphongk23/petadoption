package com.projects.hanoipetadoption.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projects.hanoipetadoption.data.model.PetCharacteristicEntity

/**
 * DAO for pet characteristics
 */
@Dao
interface PetCharacteristicDao {
    @Query("SELECT * FROM pet_characteristics WHERE petId = :petId")
    suspend fun getCharacteristicsForPet(petId: String): List<PetCharacteristicEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacteristics(characteristics: List<PetCharacteristicEntity>)
    
    @Query("DELETE FROM pet_characteristics WHERE petId = :petId")
    suspend fun deleteCharacteristicsForPet(petId: String)
} 