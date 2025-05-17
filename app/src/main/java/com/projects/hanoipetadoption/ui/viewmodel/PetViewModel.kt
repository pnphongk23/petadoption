package com.projects.hanoipetadoption.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.domain.repository.PetRepository
import com.projects.hanoipetadoption.data.repository.AdoptionRepository
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.ui.model.AdoptionApplication
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetsUiState
import com.projects.hanoipetadoption.ui.model.PetWithAdoptionStatus
import com.projects.hanoipetadoption.data.mapper.toPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PetViewModel(
    private val petRepository: PetRepository,
    private val adoptionRepository: AdoptionRepository
) : ViewModel() {

    // UiState for the PetsScreen
    private val _uiState = MutableStateFlow<PetsUiState>(PetsUiState.Loading)
    val uiState: StateFlow<PetsUiState> = _uiState.asStateFlow()
    
    // Derived state for adopted pets only
    val adoptedPets: StateFlow<List<PetWithAdoptionStatus>> = uiState
        .map { state ->
            if (state is PetsUiState.Success) {
                state.pets.filter { it.isAdopted }
            } else {
                emptyList()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadPetsWithStatus()
    }

    private fun loadPetsWithStatus() {
        viewModelScope.launch {
            _uiState.value = PetsUiState.Loading
            
            petRepository.getAllPetsWithAdoptionStatus()
                .collect { result ->
                    result.fold(
                        onSuccess = { petsWithStatus ->
                            _uiState.value = PetsUiState.Success(petsWithStatus)
                        },
                        onFailure = { error ->
                            _uiState.value = PetsUiState.Error(error.message ?: "Unknown error occurred")
                        }
                    )
                }
        }
    }

    fun submitAdoptionApplication(application: AdoptionApplication) {
        viewModelScope.launch {
            adoptionRepository.submitApplication(application)
            // Refresh the pets list after submitting an application
            loadPetsWithStatus()
        }
    }
    
    // Function to refresh the pets list
    fun refreshPets() {
        loadPetsWithStatus()
    }
}

