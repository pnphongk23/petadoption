package com.projects.hanoipetadoption.ui.viewmodel.myadoptedpets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.data.mapper.toPresentation
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import com.projects.hanoipetadoption.data.model.postadoption.Reminder // For the StateFlow
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.domain.repository.PetRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.HealthRecordRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.StatusRepository
// TODO: Import your actual Pet model
import com.projects.hanoipetadoption.ui.model.Pet
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File // For image handling
import java.util.Date // For PetStatusUpdate

/**
 * ViewModel for the AdoptedPetHub screen
 */
// TODO: Set up actual Dependency Injection (e.g., Hilt, Koin) to provide these repositories
class AdoptedPetHubViewModel(
    private val statusRepository: StatusRepository,
    private val reminderRepository: ReminderRepository,
    private val healthRecordRepository: HealthRecordRepository,
     private val petRepository: PetRepository // If needed for basic pet details
) : ViewModel() {

    private val _petDetails = MutableStateFlow<Pet?>(null)
    val petDetails: StateFlow<Pet?> = _petDetails.asStateFlow()

    private val _statusUpdates = MutableStateFlow<List<PetStatusUpdate>>(emptyList())
    val statusUpdates: StateFlow<List<PetStatusUpdate>> = _statusUpdates.asStateFlow()

    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList()) // Changed from List<Any>
    val reminders: StateFlow<List<Reminder>> = _reminders.asStateFlow()

    private val _healthRecords = MutableStateFlow<List<HealthRecord>>(emptyList())
    val healthRecords: StateFlow<List<HealthRecord>> = _healthRecords.asStateFlow()

    // TODO: Add StateFlows for loading states (e.g., _isLoading, _errorMessages)

    fun loadPetDetails(petId: String) {
        viewModelScope.launch {
            // TODO: Set _isLoading true
            try {
                // Fetch basic pet details if needed (e.g., from a general PetRepository)
                // For now, using the sample. Replace with actual repository call.
                // _petDetails.value = petRepository.getPetById(petId.toString()).getOrNull() ?: getSamplePet(petId)
                 _petDetails.value = getSamplePet(petId) // Using sample for now

                // Load related post-adoption data
                loadStatusUpdates(petId)
                loadReminders(petId)
                loadHealthRecords(petId)
            } catch (e: Exception) {
                _petDetails.value = null // Clear on error or set an error state
                // TODO: Update error StateFlow
            } finally {
                // TODO: Set _isLoading false
            }
        }
    }

    fun loadStatusUpdates(petId: String) {
        viewModelScope.launch {
            // TODO: Handle loading state for this specific list
            statusRepository.getPetStatusUpdates(petId).onSuccess {
                _statusUpdates.value = it
            }.onFailure {
                // TODO: Update error StateFlow for status updates
                _statusUpdates.value = emptyList() // Clear or show previous state
            }
        }
    }

    fun loadReminders(petId: String) {
        viewModelScope.launch {
            reminderRepository.getRemindersForPet(petId).onSuccess {
                _reminders.value = it
            }.onFailure {
                // TODO: Update error StateFlow for reminders
                _reminders.value = emptyList()
            }
        }
    }

    fun loadHealthRecords(petId: String) {
        viewModelScope.launch {
            healthRecordRepository.getHealthRecordsForPet(petId).onSuccess {
                _healthRecords.value = it
            }.onError {
                // TODO: Update error StateFlow for health records
                _healthRecords.value = emptyList()
            }
        }
    }

    fun addPetStatusUpdate(petId: String, statusUpdateContent: PetStatusUpdate, images: List<File>) {
        viewModelScope.launch {
            // The 'statusUpdateContent' from dialog has 'imageUrls = null'.
            // The 'StatusRepository' implementation should handle:
            // 1. Saving 'images: List<File>' (which are cached files from dialog) to permanent storage.
            // 2. Updating the 'imageUrls' field in 'statusUpdateContent' with the new paths.
            // 3. Saving the modified 'statusUpdateContent' to the database.
            // The repository's addStatusUpdate might directly take 'images' and do this internally.
            val updateWithPetId = statusUpdateContent.copy(petId = petId, createdAt = Date())

            statusRepository.addStatusUpdate(updateWithPetId, images).onSuccess { createdStatusUpdate ->
                // Refresh the list
                // Option 1: Add to current list (if repository returns the created item with ID)
                 _statusUpdates.value = listOf(createdStatusUpdate) + _statusUpdates.value
                // Option 2: Reload all (simpler, but less efficient for UI)
                // loadStatusUpdates(petId)
            }.onFailure {
                // TODO: Update error StateFlow / show error message to user
            }
        }
    }

    fun addReminder(reminderCreate: VaccinationReminderCreate) {
        viewModelScope.launch {
            // Assuming reminderCreate.petId is valid and set from the dialog/screen
            reminderRepository.createVaccinationReminder(reminderCreate).onSuccess { createdReminder ->
                 _reminders.value = listOf(createdReminder) + _reminders.value
                // loadReminders(reminderCreate.petId) // Or reload
            }.onFailure {
                // TODO: Update error StateFlow / show error message to user
            }
        }
    }

    fun addHealthRecord(healthRecordCreate: HealthRecordCreate) {
        viewModelScope.launch {
            // Convert HealthRecordCreate to HealthRecord
            val healthRecordToCreate = HealthRecord(
                id = null, // DB will generate
                petId = healthRecordCreate.petId,
                userId = null, // Or get from current session if needed by repo/backend
                recordType = healthRecordCreate.recordType,
                notes = healthRecordCreate.notes,
                weight = healthRecordCreate.weight,
                recordDate = healthRecordCreate.recordDate,
                nextReminderDate = healthRecordCreate.nextReminderDate,
                mediaItems = emptyList() // New record won't have media initially
            )

            healthRecordRepository.createHealthRecord(healthRecordToCreate).onSuccess { createdHealthRecord ->
                _healthRecords.value = listOf(createdHealthRecord) + _healthRecords.value
                // loadHealthRecords(healthRecordCreate.petId) // Or reload
            }.onError {
                // TODO: Update error StateFlow / show error message to user
            }
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    private suspend fun getSamplePet(petId: String): Pet = coroutineScope {
        suspendCancellableCoroutine { conn ->
            launch {
                 petRepository.getPetById(petId).onSuccess { conn.tryResume(it.toPresentation()) }
            }
            
        }
    }
} 