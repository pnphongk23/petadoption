package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.domain.repository.postadoption.HealthRepository

/**
 * Use case to create a health record
 */
class CreateHealthRecordUseCase(private val repository: HealthRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(healthRecord: HealthRecordCreate): Result<HealthRecord> {
        return repository.createHealthRecord(healthRecord)
    }
}
