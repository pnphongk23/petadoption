package com.projects.hanoipetadoption.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.projects.hanoipetadoption.data.model.PetEntity
import com.projects.hanoipetadoption.data.model.PetWithAdoptionStatusEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Pet table
 */
@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    suspend fun getAllPets(): List<PetEntity>
    
    @Query("SELECT * FROM pets WHERE id = :petId")
    suspend fun getPetById(petId: String): PetEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPets(pets: List<PetEntity>)
    
    @Update
    suspend fun updatePet(pet: PetEntity)
    
    @Query("UPDATE pets SET isFavorite = NOT isFavorite WHERE id = :petId")
    suspend fun toggleFavorite(petId: String): Int
    
    @Delete
    suspend fun deletePet(pet: PetEntity)
    
    @Query("DELETE FROM pets WHERE id = :petId")
    suspend fun deletePetById(petId: String)
    
    /**
     * Gets all pets with their adoption status via a LEFT JOIN
     * This query is more efficient than combining two separate queries
     */
    @Transaction
    @Query("""
        SELECT p.*, 
            CASE WHEN a.petId IS NOT NULL THEN 1 ELSE 0 END as isAdopted
        FROM pets p
        LEFT JOIN (
            SELECT DISTINCT petId 
            FROM adoption_applications 
            WHERE status = 'APPROVED'
        ) a ON p.id = a.petId
    """)
    fun getAllPetsWithAdoptionStatus(): Flow<List<PetWithAdoptionStatusEntity>>
} 