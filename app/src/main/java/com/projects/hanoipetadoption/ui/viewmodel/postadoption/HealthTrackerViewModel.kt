package com.projects.hanoipetadoption.ui.viewmodel.postadoption

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecord
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordMedia
import com.projects.hanoipetadoption.data.model.postadoption.MediaType
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.domain.usecase.postadoption.AddMediaToHealthRecordUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.CreateHealthRecordUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetHealthRecordsForPetUseCase
import java.io.File
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

/**
 * State holder for health records
 */
sealed class HealthRecordsState {
    object Loading : HealthRecordsState() // Initial state can be Loading or a more specific Idle state
    data class Success(val records: List<HealthRecord>) : HealthRecordsState()
    data class Error(val message: String) : HealthRecordsState()
    // data class Idle: HealthRecordsState() // Consider adding an Idle state if Loading isn't always the initial
}

/**
 * ViewModel for health tracking
 */
class HealthTrackerViewModel(
    private val getHealthRecordsForPetUseCase: GetHealthRecordsForPetUseCase,
    private val createHealthRecordUseCase: CreateHealthRecordUseCase,
    private val addMediaToHealthRecordUseCase: AddMediaToHealthRecordUseCase
) : ViewModel() {

    private val _healthRecordsState = MutableStateFlow<HealthRecordsState>(HealthRecordsState.Loading) // Initialized to Loading
    val healthRecordsState: StateFlow<HealthRecordsState> = _healthRecordsState.asStateFlow()

    private val _mediaUploadState = MutableStateFlow<HealthRecordMedia?>(null)
    val mediaUploadState: StateFlow<HealthRecordMedia?> = _mediaUploadState.asStateFlow()

    private val _mediaUploadError = MutableSharedFlow<String>(
        replay = 0, 
        extraBufferCapacity = 1, 
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val mediaUploadError: SharedFlow<String> = _mediaUploadError.asSharedFlow()

    private var currentPetId: String? = null

    /**
     * Load health records for a pet
     */
    fun loadHealthRecords(
        petId: String,
        recordType: RecordType? = null,
        startDate: Date? = null,
        endDate: Date? = null
    ) {
        this.currentPetId = petId 
        _healthRecordsState.value = HealthRecordsState.Loading
        viewModelScope.launch {
            val result = getHealthRecordsForPetUseCase.invoke(petId, recordType, startDate, endDate)
            result.onSuccess { records ->
                _healthRecordsState.value = HealthRecordsState.Success(records)
            }.onError { error ->
                _healthRecordsState.value = HealthRecordsState.Error(
                    error.message ?: "Failed to load health records"
                )
            }
        }
    }

    /**
     * Create a new health record
     */
    fun createHealthRecord(healthRecord: HealthRecordCreate, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            createHealthRecordUseCase(healthRecord)
                .onSuccess {
                    loadHealthRecords(healthRecord.petId)
                    onComplete(true)
                }.onError {
                    // Consider emitting to a SharedFlow for creation errors too if UI needs to show a message
                    onComplete(false)
                }
        }
    }

    /**
     * Add media to a health record
     */
    fun addMediaToHealthRecord(recordId: Int, file: File, mediaType: MediaType) {
        viewModelScope.launch {
            addMediaToHealthRecordUseCase(recordId, file, mediaType)
                .fold(
                    onSuccess = { media ->
                        _mediaUploadState.value = media
                        currentPetId?.let { pid ->
                            loadHealthRecords(pid)
                        }
                    },
                    onFailure = { error ->
                        _mediaUploadState.value = null // Clear any previous success state for media
                        _mediaUploadError.tryEmit(error.message ?: "Media upload failed")
                    }
                )
        }
    }

    /**
     * Reset media upload state (e.g., after UI has consumed the state)
     */
    fun resetMediaUploadState() {
        _mediaUploadState.value = null
    }

    // clearMediaUploadError() is removed as SharedFlow events are consumed, not cleared from ViewModel state.
}
