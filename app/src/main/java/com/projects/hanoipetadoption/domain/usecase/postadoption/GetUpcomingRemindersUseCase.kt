package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository

/**
 * Use case to get upcoming reminders
 */
class GetUpcomingRemindersUseCase(private val repository: ReminderRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(daysAhead: Int = 7): Result<List<Reminder>> {
        return repository.getUpcomingReminders(daysAhead)
    }
}
