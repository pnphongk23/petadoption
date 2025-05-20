package com.projects.hanoipetadoption.ui.viewmodel.myadoptedpets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.data.mapper.toPresentation
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import com.projects.hanoipetadoption.data.model.postadoption.VaccinationReminderCreate
import com.projects.hanoipetadoption.domain.repository.PetRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.HealthRecordRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.StatusRepository
// TODO: Import your actual Pet model
import com.projects.hanoipetadoption.ui.model.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File // For image handling
import java.util.Date // For PetStatusUpdate
import kotlin.coroutines.resume

class AdoptedPetHubViewModel(
    private val statusRepository: StatusRepository,
    private val reminderRepository: ReminderRepository,
    private val healthRecordRepository: HealthRecordRepository,
    private val petRepository: PetRepository // If needed for basic pet details
) : ViewModel() {

    private val _petDetails = MutableStateFlow<Pet?>(null)
    val petDetails: StateFlow<Pet?> = _petDetails.asStateFlow()

    private val _statusUpdates = MutableStateFlow<List<PetStatusUpdate>>(emptyList())

    private val _healthRecords = MutableStateFlow<List<HealthRecord>>(emptyList())


    fun loadPetDetails(petId: String) {
        viewModelScope.launch {
            try {
                _petDetails.value = getPetById(petId) // Using sample for now
            } catch (e: Exception) {
                _petDetails.value = null // Clear on error or set an error state
            } finally {
            }
        }
    }

    fun addPetStatusUpdate(
        petId: String,
        statusUpdateContent: PetStatusUpdate,
        images: List<File>
    ) {
        viewModelScope.launch {
            val updateWithPetId = statusUpdateContent.copy(petId = petId, createdAt = Date())
            statusRepository.addStatusUpdate(updateWithPetId, images)
                .onSuccess { createdStatusUpdate ->
                    _statusUpdates.value = listOf(createdStatusUpdate) + _statusUpdates.value
                }.onFailure {

            }
        }
    }

    fun addReminder(reminderCreate: VaccinationReminderCreate) {
        viewModelScope.launch {
            reminderRepository.createVaccinationReminder(reminderCreate)
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

            healthRecordRepository.createHealthRecord(healthRecordToCreate)
                .onSuccess { createdHealthRecord ->
                    _healthRecords.value = listOf(createdHealthRecord) + _healthRecords.value
                    // loadHealthRecords(healthRecordCreate.petId) // Or reload
                }.onError {
                // TODO: Update error StateFlow / show error message to user
            }
        }
    }

    private suspend fun getPetById(petId: String): Pet? = suspendCancellableCoroutine { conn ->
        viewModelScope.launch(Dispatchers.IO) {
            petRepository.getPetById(petId).fold(
                onSuccess = { conn.resume(it.toPresentation()) },
                onFailure = { conn.resume(null) } // Handle error case
            )
        }
    }
} 