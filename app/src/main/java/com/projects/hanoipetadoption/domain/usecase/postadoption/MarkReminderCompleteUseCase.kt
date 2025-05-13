package com.projects.hanoipetadoption.domain.usecase.postadoption

import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository

/**
 * Use case to mark a reminder as complete
 */
class MarkReminderCompleteUseCase(private val repository: ReminderRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(reminderId: Int): Result<Boolean> {
        return repository.markReminderComplete(reminderId)
    }
}
