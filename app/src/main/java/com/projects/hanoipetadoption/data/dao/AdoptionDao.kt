package com.projects.hanoipetadoption.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projects.hanoipetadoption.data.model.AdoptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdoptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: AdoptionEntity)

    @Query("SELECT * FROM adoption_applications WHERE status = 'APPROVED'")
    fun getApprovedApplications(): Flow<List<AdoptionEntity>>

    @Query("SELECT petId FROM adoption_applications WHERE status = 'APPROVED'")
    fun getAdoptedPetIds(): Flow<List<String>>
}
