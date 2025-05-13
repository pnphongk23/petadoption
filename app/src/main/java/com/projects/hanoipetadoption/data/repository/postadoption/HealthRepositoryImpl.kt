package com.projects.hanoipetadoption.data.repository.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordUpdate
import com.projects.hanoipetadoption.data.model.postadoption.MediaType
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.data.source.postadoption.HealthLocalDataSource
import com.projects.hanoipetadoption.data.source.postadoption.HealthRemoteDataSource
import com.projects.hanoipetadoption.domain.repository.postadoption.HealthRepository
import java.io.File
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of HealthRepository
 */
class HealthRepositoryImpl(
    private val remoteDataSource: HealthRemoteDataSource,
    private val localDataSource: HealthLocalDataSource
) : HealthRepository {
    
    override suspend fun getHealthRecordsForPet(
        petId: Int,
        recordType: RecordType?,
        startDate: Date?,
        endDate: Date?
    ): Result<List<HealthRecord>> = withContext(Dispatchers.IO) {
        return@withContext try {
            // Try to fetch from remote
            val remoteRecords = remoteDataSource.getHealthRecordsForPet(
                petId, recordType, startDate, endDate
            )
            
            // Cache the results
            localDataSource.saveHealthRecords(remoteRecords)
            
            Result.success(remoteRecords)
        } catch (e: Exception) {
            // If remote fails, try to get from cache
            try {
                val cachedRecords = localDataSource.getHealthRecordsForPet(petId)
                if (cachedRecords.isNotEmpty()) {
                    Result.success(cachedRecords)
                } else {
                    Result.failure(e)
                }
            } catch (cacheException: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getHealthRecordById(recordId: Int): Result<HealthRecord?> = 
        withContext(Dispatchers.IO) {
            return@withContext try {
                val record = remoteDataSource.getHealthRecordById(recordId)
                Result.success(record)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun createHealthRecord(healthRecord: HealthRecordCreate): Result<HealthRecord> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val createdRecord = remoteDataSource.createHealthRecord(healthRecord)
                Result.success(createdRecord)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun updateHealthRecord(
        recordId: Int,
        update: HealthRecordUpdate
    ): Result<HealthRecord> = withContext(Dispatchers.IO) {
        return@withContext try {
            val updatedRecord = remoteDataSource.updateHealthRecord(recordId, update)
            Result.success(updatedRecord)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteHealthRecord(recordId: Int): Result<Boolean> = 
        withContext(Dispatchers.IO) {
            return@withContext try {
                val success = remoteDataSource.deleteHealthRecord(recordId)
                Result.success(success)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun addMediaToHealthRecord(
        recordId: Int,
        file: File,
        mediaType: MediaType
    ): Result<HealthRecordMedia> = withContext(Dispatchers.IO) {
        return@withContext try {
            val media = remoteDataSource.addMediaToHealthRecord(recordId, file, mediaType)
            Result.success(media)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMediaForHealthRecord(recordId: Int): Result<List<HealthRecordMedia>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                // Try to fetch from remote
                val remoteMedia = remoteDataSource.getMediaForHealthRecord(recordId)
                
                // Cache the results
                localDataSource.saveMediaForHealthRecord(recordId, remoteMedia)
                
                Result.success(remoteMedia)
            } catch (e: Exception) {
                // If remote fails, try to get from cache
                try {
                    val cachedMedia = localDataSource.getMediaForHealthRecord(recordId)
                    if (cachedMedia.isNotEmpty()) {
                        Result.success(cachedMedia)
                    } else {
                        Result.failure(e)
                    }
                } catch (cacheException: Exception) {
                    Result.failure(e)
                }
            }
        }

    override suspend fun deleteMediaFromHealthRecord(
        recordId: Int,
        mediaId: Int
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            val success = remoteDataSource.deleteMediaFromHealthRecord(recordId, mediaId)
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
