package com.projects.hanoipetadoption.presentation.viewmodel.postadoption

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.domain.usecase.postadoption.CreateVaccinationReminderUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetRemindersForPetUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetUpcomingRemindersUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.MarkReminderCompleteUseCase
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

    private val _remindersState = MutableLiveData<ReminderState>()
    val remindersState: LiveData<ReminderState> = _remindersState

    private val _upcomingRemindersState = MutableLiveData<ReminderState>()
    val upcomingRemindersState: LiveData<ReminderState> = _upcomingRemindersState

    /**
     * Load reminders for a pet
     */
    fun loadRemindersForPet(petId: Int) {
        _remindersState.value = ReminderState.Loading
        viewModelScope.launch {
            getRemindersForPetUseCase(petId)
                .fold(
                    onSuccess = { reminders ->
                        _remindersState.value = ReminderState.Success(reminders)
                    },
                    onFailure = { error ->
                        _remindersState.value = ReminderState.Error(
                            error.message ?: "Failed to load reminders"
                        )
                    }
                )
        }
    }

    /**
     * Load upcoming reminders
     */
    fun loadUpcomingReminders(daysAhead: Int = 7) {
        _upcomingRemindersState.value = ReminderState.Loading
        viewModelScope.launch {
            getUpcomingRemindersUseCase(daysAhead)
                .fold(
                    onSuccess = { reminders ->
                        _upcomingRemindersState.value = ReminderState.Success(reminders)
                    },
                    onFailure = { error ->
                        _upcomingRemindersState.value = ReminderState.Error(
                            error.message ?: "Failed to load upcoming reminders"
                        )
                    }
                )
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
                    onSuccess = { createdReminder ->
                        // Reload reminders after creating a new one
                        loadRemindersForPet(reminder.petId)
                        loadUpcomingReminders()
                        onComplete(true)
                    },
                    onFailure = { error ->
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
                            // Refresh the reminders after marking one as complete
                            val currentReminders = _remindersState.value
                            if (currentReminders is ReminderState.Success) {
                                val updatedReminders = currentReminders.reminders.map { 
                                    if (it.id == reminderId) it.copy(isCompleted = true) else it 
                                }
                                _remindersState.value = ReminderState.Success(updatedReminders)
                            }

                            val currentUpcoming = _upcomingRemindersState.value
                            if (currentUpcoming is ReminderState.Success) {
                                val updatedUpcoming = currentUpcoming.reminders.map { 
                                    if (it.id == reminderId) it.copy(isCompleted = true) else it 
                                }
                                _upcomingRemindersState.value = ReminderState.Success(updatedUpcoming)
                            }
                        }
                        onComplete(success)
                    },
                    onFailure = { error ->
                        onComplete(false)
                    }
                )
        }
    }
}
