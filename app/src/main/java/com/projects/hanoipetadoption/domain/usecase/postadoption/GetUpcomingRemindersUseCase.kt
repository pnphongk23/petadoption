package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get upcoming reminders
 */
class GetUpcomingRemindersUseCase(private val repository: ReminderRepository) {
    /**
     * Execute the use case
     * @param daysAhead Number of days to look ahead for reminders
     * @return A Flow emitting a list of upcoming reminders
     */
    operator fun invoke(daysAhead: Int): Flow<List<Reminder>> {
        return repository.getUpcomingReminders(daysAhead)
    }
}
