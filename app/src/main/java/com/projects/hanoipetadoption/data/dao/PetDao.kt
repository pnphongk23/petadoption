package com.projects.hanoipetadoption.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.projects.hanoipetadoption.data.model.PetEntity

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
} 