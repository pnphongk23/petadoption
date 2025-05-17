package com.projects.hanoipetadoption.ui.viewmodel.postadoption

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetCareInstructionsUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.OpenCareDocumentUseCase
import kotlinx.coroutines.launch

/**
 * State holder for care instructions
 */
sealed class CareInstructionsState {
    object Loading : CareInstructionsState()
    data class Success(val instructions: List<CareInstructions>) : CareInstructionsState()
    data class Error(val message: String) : CareInstructionsState()
}

/**
 * ViewModel for care instructions
 */
class CareInstructionsViewModel(
    private val getCareInstructionsUseCase: GetCareInstructionsUseCase,
    private val openCareDocumentUseCase: OpenCareDocumentUseCase
) : ViewModel() {

    private val _careInstructionsState = MutableLiveData<CareInstructionsState>()
    val careInstructionsState: LiveData<CareInstructionsState> = _careInstructionsState

    /**
     * Load care instructions for a pet
     */
    fun loadCareInstructions(petId: String) {
        _careInstructionsState.value = CareInstructionsState.Loading
        viewModelScope.launch {
            getCareInstructionsUseCase(petId)
                .fold(
                    onSuccess = { instructions ->
                        _careInstructionsState.value = CareInstructionsState.Success(instructions)
                    },
                    onFailure = { error ->
                        _careInstructionsState.value = CareInstructionsState.Error(
                            error.message ?: "Failed to load care instructions"
                        )
                    }
                )
        }
    }

    /**
     * Open a care instruction document using the system's intent
     */
    fun openDocument(context: Context, instruction: CareInstructions, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            openCareDocumentUseCase(context, instruction)
                .fold(
                    onSuccess = { success ->
                        onComplete(success)
                    },
                    onFailure = { error ->
                        onComplete(false)
                    }
                )
        }
    }
}
