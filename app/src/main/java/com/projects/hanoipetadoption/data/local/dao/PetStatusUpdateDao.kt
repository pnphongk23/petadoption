package com.projects.hanoipetadoption.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.projects.hanoipetadoption.data.local.entity.PetStatusUpdateEntity
import com.projects.hanoipetadoption.data.local.entity.PetStatusUpdateImageEntity
import com.projects.hanoipetadoption.data.local.relation.PetStatusUpdateWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface PetStatusUpdateDao {

    // PetStatusUpdateEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPetStatusUpdate(statusUpdate: PetStatusUpdateEntity): Long

    @Update
    suspend fun updatePetStatusUpdate(statusUpdate: PetStatusUpdateEntity)

    @Query("SELECT * FROM pet_status_updates WHERE id = :updateId")
    suspend fun getPetStatusUpdateEntityById(updateId: Long): PetStatusUpdateEntity?

    @Query("DELETE FROM pet_status_updates WHERE id = :updateId")
    suspend fun deletePetStatusUpdateById(updateId: Long)

    // PetStatusUpdateImageEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPetStatusUpdateImage(image: PetStatusUpdateImageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPetStatusUpdateImages(images: List<PetStatusUpdateImageEntity>)

    @Query("SELECT * FROM pet_status_update_images WHERE status_update_id = :updateId")
    fun getImagesForPetStatusUpdateFlow(updateId: Long): Flow<List<PetStatusUpdateImageEntity>>
    
    @Query("SELECT * FROM pet_status_update_images WHERE status_update_id = :updateId")
    suspend fun getImagesForPetStatusUpdate(updateId: Long): List<PetStatusUpdateImageEntity>

    @Query("DELETE FROM pet_status_update_images WHERE id = :imageId")
    suspend fun deleteImageById(imageId: Long)

    @Query("DELETE FROM pet_status_update_images WHERE status_update_id = :updateId")
    suspend fun deleteAllImagesForUpdate(updateId: Long)

    // Operations with relations
    @Transaction
    @Query("SELECT * FROM pet_status_updates WHERE id = :updateId")
    suspend fun getPetStatusUpdateWithImagesById(updateId: Long): PetStatusUpdateWithImages?

    @Transaction
    @Query("SELECT * FROM pet_status_updates WHERE pet_id = :petId ORDER BY created_at DESC")
    fun getPetStatusUpdatesWithImagesForPet(petId: String): Flow<List<PetStatusUpdateWithImages>>

} 