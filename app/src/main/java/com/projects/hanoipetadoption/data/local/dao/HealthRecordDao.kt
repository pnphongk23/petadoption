package com.projects.hanoipetadoption.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.projects.hanoipetadoption.data.local.entity.HealthRecordEntity
import com.projects.hanoipetadoption.data.local.entity.HealthRecordMediaEntity
import com.projects.hanoipetadoption.data.local.relation.HealthRecordWithMedia
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {

    // HealthRecordEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecord(healthRecord: HealthRecordEntity): Long

    @Update
    suspend fun updateHealthRecord(healthRecord: HealthRecordEntity)

    @Query("SELECT * FROM health_records WHERE id = :recordId")
    suspend fun getHealthRecordEntityById(recordId: Long): HealthRecordEntity?

    @Query("DELETE FROM health_records WHERE id = :recordId")
    suspend fun deleteHealthRecordById(recordId: Long)

    // HealthRecordMediaEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecordMedia(media: HealthRecordMediaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHealthRecordMedia(mediaItems: List<HealthRecordMediaEntity>)

    @Query("SELECT * FROM health_record_media WHERE health_record_id = :recordId")
    fun getMediaForHealthRecordFlow(recordId: Long): Flow<List<HealthRecordMediaEntity>>

    @Query("SELECT * FROM health_record_media WHERE health_record_id = :recordId")
    suspend fun getMediaForHealthRecord(recordId: Long): List<HealthRecordMediaEntity>
    
    @Query("DELETE FROM health_record_media WHERE id = :mediaId")
    suspend fun deleteMediaById(mediaId: Long)

    @Query("DELETE FROM health_record_media WHERE health_record_id = :recordId")
    suspend fun deleteAllMediaForRecord(recordId: Long)

    // Operations with relations
    @Transaction
    @Query("SELECT * FROM health_records WHERE id = :recordId")
    suspend fun getHealthRecordWithMediaById(recordId: Long): HealthRecordWithMedia?

    @Transaction
    @Query("SELECT * FROM health_records WHERE pet_id = :petId ORDER BY record_date DESC")
    fun getHealthRecordsWithMediaForPet(petId: String): Flow<List<HealthRecordWithMedia>>
    
    @Transaction
    @Query("SELECT * FROM health_records WHERE pet_id = :petId AND record_type = :recordType ORDER BY record_date DESC")
    fun getHealthRecordsWithMediaForPetFilteredByType(petId: String, recordType: RecordType): Flow<List<HealthRecordWithMedia>>

} 