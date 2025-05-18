package com.projects.hanoipetadoption.ui.viewmodel.postadoption

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.data.mapper.toEntity
import com.projects.hanoipetadoption.data.mapper.toModelSimple
import com.projects.hanoipetadoption.data.mapper.toReminder
import com.projects.hanoipetadoption.data.mapper.toReminderEntity
import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.domain.usecase.postadoption.CreateVaccinationReminderUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetRemindersForPetUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetUpcomingRemindersUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.MarkReminderCompleteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * State holder for reminders
 */
sealed class ReminderState {
    object Loading : ReminderState()
    data class Success(val reminders: List<Reminder>) : ReminderState()
    data class Error(val message: String) : ReminderState()
}

/**
 * ViewModel for reminders
 */
class ReminderViewModel(
    private val getUpcomingRemindersUseCase: GetUpcomingRemindersUseCase,
    private val getRemindersForPetUseCase: GetRemindersForPetUseCase,
    private val createVaccinationReminderUseCase: CreateVaccinationReminderUseCase,
    private val markReminderCompleteUseCase: MarkReminderCompleteUseCase
) : ViewModel() {

    private val _remindersState = MutableStateFlow<ReminderState>(ReminderState.Loading)
    val remindersState: StateFlow<ReminderState> = _remindersState.asStateFlow()

    private val _upcomingRemindersState = MutableStateFlow<ReminderState>(ReminderState.Loading)
    val upcomingRemindersState: StateFlow<ReminderState> = _upcomingRemindersState.asStateFlow()

    /**
     * Load reminders for a pet
     */
    fun loadRemindersForPet(petId: String) {
        viewModelScope.launch {
            getRemindersForPetUseCase(petId)
                .onStart { _remindersState.value = ReminderState.Loading }
                .map { reminders -> ReminderState.Success(reminders) }
                .catch { e -> _remindersState.value = ReminderState.Error(e.message ?: "Failed to load reminders") }
                .collect { state -> _remindersState.value = state }
        }
    }

    /**
     * Load upcoming reminders
     */
    fun loadUpcomingReminders(daysAhead: Int = 7) {
        viewModelScope.launch {
            getUpcomingRemindersUseCase(daysAhead)
                .onStart { _upcomingRemindersState.value = ReminderState.Loading }
                .map { reminders -> ReminderState.Success(reminders) }
                .catch { e -> _upcomingRemindersState.value = ReminderState.Error(e.message ?: "Failed to load upcoming reminders") }
                .collect { state -> _upcomingRemindersState.value = state }
        }
    }

    /**
     * Create a vaccination reminder
     */
    fun createVaccinationReminder(
        reminder: VaccinationReminderCreate,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            createVaccinationReminderUseCase(reminder)
                .fold(
                    onSuccess = {
                        loadRemindersForPet(reminder.petId)
                        loadUpcomingReminders()
                        onComplete(true)
                    },
                    onFailure = { _ ->
                        onComplete(false)
                    }
                )
        }
    }

    /**
     * Mark a reminder as complete
     */
    fun markReminderComplete(reminderId: Int, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            markReminderCompleteUseCase(reminderId)
                .fold(
                    onSuccess = { success ->
                        if (success) {
                            val currentPetReminders = _remindersState.value
                            if (currentPetReminders is ReminderState.Success) {
                                val updatedList = currentPetReminders.reminders.map {
                                    if (it.id == reminderId) it.copy(isCompleted = true) else it
                                }
                                _remindersState.value = ReminderState.Success(updatedList)
                            }

                            val currentUpcoming = _upcomingRemindersState.value
                            if (currentUpcoming is ReminderState.Success) {
                                val updatedList = currentUpcoming.reminders.map {
                                    if (it.id == reminderId) it.copy(isCompleted = true) else it
                                }
                                _upcomingRemindersState.value = ReminderState.Success(updatedList)
                            }
                        }
                        onComplete(success)
                    },
                    onFailure = { _ ->
                        onComplete(false)
                    }
                )
        }
    }
}
