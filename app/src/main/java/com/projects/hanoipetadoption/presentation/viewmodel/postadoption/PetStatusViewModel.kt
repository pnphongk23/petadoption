package com.projects.hanoipetadoption.presentation.viewmodel.postadoption

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import com.projects.hanoipetadoption.domain.usecase.postadoption.AddPetStatusUpdateUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetPetStatusUpdatesUseCase
import kotlinx.coroutines.launch
import java.io.File

/**
 * State holder for pet status updates
 */
sealed class PetStatusState {
    object Loading : PetStatusState()
    data class Success(val updates: List<PetStatusUpdate>) : PetStatusState()
    data class Error(val message: String) : PetStatusState()
}

/**
 * ViewModel for pet status updates
 */
class PetStatusViewModel(
    private val getPetStatusUpdatesUseCase: GetPetStatusUpdatesUseCase,
    private val addPetStatusUpdateUseCase: AddPetStatusUpdateUseCase
) : ViewModel() {

    private val _petStatusState = MutableLiveData<PetStatusState>()
    val petStatusState: LiveData<PetStatusState> = _petStatusState

    /**
     * Load status updates for a pet
     */
    fun loadStatusUpdates(petId: Int) {
        _petStatusState.value = PetStatusState.Loading
        viewModelScope.launch {
            getPetStatusUpdatesUseCase(petId)
                .fold(
                    onSuccess = { updates ->
                        _petStatusState.value = PetStatusState.Success(updates)
                    },
                    onFailure = { error ->
                        _petStatusState.value = PetStatusState.Error(
                            error.message ?: "Failed to load status updates"
                        )
                    }
                )
        }
    }

    /**
     * Add a new status update for a pet
     */
    fun addStatusUpdate(update: PetStatusUpdate, images: List<File>? = null, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            addPetStatusUpdateUseCase(update, images)
                .fold(
                    onSuccess = { createdUpdate ->
                        // Reload status updates after creating a new one
                        loadStatusUpdates(update.petId)
                        onComplete(true)
                    },
                    onFailure = { error ->
                        onComplete(false)
                    }
                )
        }
    }
}
