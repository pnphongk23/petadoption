package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get reminders for a pet
 */
class GetRemindersForPetUseCase(private val repository: ReminderRepository) {
    /**
     * Execute the use case.
     */
    operator fun invoke(petId: String): Flow<List<Reminder>> {
        return repository.getRemindersForPet(petId = petId)
    }
}
