package com.projects.hanoipetadoption.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.projects.hanoipetadoption.data.source.PetLocalDataSourceImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Worker to initialize database with sample data on first app start
 */
class DataInitializerWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {
    
    private val petLocalDataSource: PetLocalDataSourceImpl by inject()
    
    override suspend fun doWork(): Result {
        return try {
            // Initialize database with sample data if needed
            petLocalDataSource.initializeWithSampleDataIfEmpty()
            
            Result.success()
        } catch (e: Exception) {
            // If initialization fails, retry
            Result.retry()
        }
    }
    
    companion object {
        const val WORK_NAME = "data_initializer_worker"
    }
} 