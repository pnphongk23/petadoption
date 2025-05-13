package com.projects.hanoipetadoption.presentation.viewmodel.postadoption

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.launch
import java.util.Date

/**
 * State holder for health records
 */
sealed class HealthRecordsState {
    object Loading : HealthRecordsState()
    data class Success(val records: List<HealthRecord>) : HealthRecordsState()
    data class Error(val message: String) : HealthRecordsState()
}

/**
 * ViewModel for health tracking
 */
class HealthTrackerViewModel(
    private val getHealthRecordsForPetUseCase: GetHealthRecordsForPetUseCase,
    private val createHealthRecordUseCase: CreateHealthRecordUseCase,
    private val addMediaToHealthRecordUseCase: AddMediaToHealthRecordUseCase
) : ViewModel() {

    private val _healthRecordsState = MutableLiveData<HealthRecordsState>()
    val healthRecordsState: LiveData<HealthRecordsState> = _healthRecordsState

    private val _mediaUploadState = MutableLiveData<HealthRecordMedia?>()
    val mediaUploadState: LiveData<HealthRecordMedia?> = _mediaUploadState

    /**
     * Load health records for a pet
     */
    fun loadHealthRecords(
        petId: Int,
        recordType: RecordType? = null,
        startDate: Date? = null,
        endDate: Date? = null
    ) {
        _healthRecordsState.value = HealthRecordsState.Loading
        viewModelScope.launch {
            getHealthRecordsForPetUseCase(petId, recordType, startDate, endDate)
                .fold(
                    onSuccess = { records ->
                        _healthRecordsState.value = HealthRecordsState.Success(records)
                    },
                    onFailure = { error ->
                        _healthRecordsState.value = HealthRecordsState.Error(
                            error.message ?: "Failed to load health records"
                        )
                    }
                )
        }
    }

    /**
     * Create a new health record
     */
    fun createHealthRecord(healthRecord: HealthRecordCreate, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            createHealthRecordUseCase(healthRecord)
                .fold(
                    onSuccess = { record ->
                        // Reload records after creating a new one
                        loadHealthRecords(healthRecord.petId)
                        onComplete(true)
                    },
                    onFailure = { error ->
                        onComplete(false)
                    }
                )
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
                    },
                    onFailure = { error ->
                        _mediaUploadState.value = null
                    }
                )
        }
    }

    /**
     * Reset media upload state
     */
    fun resetMediaUploadState() {
        _mediaUploadState.value = null
    }
}
