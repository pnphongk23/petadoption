package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository

/**
 * Use case to get reminders for a pet
 */
class GetRemindersForPetUseCase(private val repository: ReminderRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(petId: String): Result<List<Reminder>> {
        return repository.getRemindersForPet(petId)
    }
}
