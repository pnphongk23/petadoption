package com.projects.hanoipetadoption.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.projects.hanoipetadoption.data.model.AdoptedPetView
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
    
    /**
     * Gets adopted pets by joining the pets and adoption_applications tables
     * Returns a combined view with pet details and adoption information
     */
    @Transaction
    @Query("""
        SELECT p.*, a.id as adoptionId, a.applicationDate 
        FROM pets p
        INNER JOIN adoption_applications a ON p.id = a.petId
        WHERE a.status = 'APPROVED'
        ORDER BY a.applicationDate DESC
    """)
    fun getAdoptedPets(): Flow<List<AdoptedPetView>>
}
