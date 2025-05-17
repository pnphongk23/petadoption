package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.domain.repository.postadoption.HealthRepository
import java.util.Date

/**
 * Use case to get health records for a pet
 */
class GetHealthRecordsForPetUseCase(private val repository: HealthRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(
        petId: String,
        recordType: RecordType? = null,
        startDate: Date? = null,
        endDate: Date? = null
    ): Result<List<HealthRecord>> {
        return repository.getHealthRecordsForPet(petId, recordType, startDate, endDate)
    }
}
