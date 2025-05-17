package com.projects.hanoipetadoption.data.repository.postadoption

import android.content.Context
import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.data.source.postadoption.CareLocalDataSource
import com.projects.hanoipetadoption.data.source.postadoption.CareLocalDataSourceImpl
import com.projects.hanoipetadoption.data.source.postadoption.CareRemoteDataSource
import com.projects.hanoipetadoption.domain.repository.postadoption.CareRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of CareRepository
 */
class CareRepositoryImpl(
    private val remoteDataSource: CareRemoteDataSource,
    private val localDataSource: CareLocalDataSource
) : CareRepository {    override suspend fun getCareInstructionsForPet(petId: String): Result<List<CareInstructions>> = 
        withContext(Dispatchers.IO) {
            return@withContext try {
                val instructions = remoteDataSource.getCareInstructionsForPet(petId)
                localDataSource.saveCareInstructions(instructions)
                Result.success(instructions)
            } catch (e: Exception) {
                try {
                    // Cast to CareLocalDataSourceImpl to access the getCachedCareInstructionsForPet method
                    val localDataSourceImpl = localDataSource as CareLocalDataSourceImpl
                    val cachedInstructions = localDataSourceImpl.getCachedCareInstructionsForPet(petId)
                    if (cachedInstructions.isNotEmpty()) {
                        Result.success(cachedInstructions)
                    } else {
                        Result.failure(e)
                    }
                } catch (cacheException: Exception) {
                    Result.failure(e)
                }
            }
        }

    override suspend fun getCareInstructionById(instructionId: Int): Result<CareInstructions?> = 
        withContext(Dispatchers.IO) {
            return@withContext try {
                val instruction = remoteDataSource.getCareInstructionById(instructionId)
                Result.success(instruction)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun openDocument(context: Context, instructions: CareInstructions): Result<Boolean> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                // Cast to CareLocalDataSourceImpl to access the openDocument method
                val localDataSourceImpl = localDataSource as CareLocalDataSourceImpl
                val success = localDataSourceImpl.openDocument(context, instructions)
                Result.success(success)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
