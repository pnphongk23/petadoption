package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository

/**
 * Use case to create a vaccination reminder
 */
class CreateVaccinationReminderUseCase(private val repository: ReminderRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(reminder: VaccinationReminderCreate): Result<Reminder> {
        return repository.createVaccinationReminder(reminder)
    }
}
