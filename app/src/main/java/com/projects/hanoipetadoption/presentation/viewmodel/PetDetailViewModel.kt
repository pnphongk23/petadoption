package com.projects.hanoipetadoption.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.data.mapper.toPresentation
import com.projects.hanoipetadoption.domain.usecase.GetPetByIdUseCase
import com.projects.hanoipetadoption.domain.usecase.TogglePetFavoriteUseCase
import com.projects.hanoipetadoption.presentation.state.PetDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Pet Detail Screen
 */
class PetDetailViewModel(
    private val getPetByIdUseCase: GetPetByIdUseCase,
    private val togglePetFavoriteUseCase: TogglePetFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PetDetailState>(PetDetailState.Loading)
    val uiState: StateFlow<PetDetailState> = _uiState.asStateFlow()

    fun loadPetDetails(petId: String) {
        viewModelScope.launch {
            _uiState.value = PetDetailState.Loading
            
            getPetByIdUseCase(petId)
                .onSuccess { petDomain ->
                    _uiState.value = PetDetailState.Success(petDomain.toPresentation())
                }
                .onFailure { error ->
                    _uiState.value = PetDetailState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun toggleFavorite(petId: String) {
        viewModelScope.launch {
            togglePetFavoriteUseCase(petId)
                .onSuccess { isSuccessful ->
                    if (isSuccessful) {
                        // Refresh the pet details after toggling favorite
                        loadPetDetails(petId)
                    }
                }
                .onFailure { error ->
                    // Handle error if needed
                    // For now, we'll just refresh the pet details to ensure UI consistency
                    loadPetDetails(petId)
                }
        }
    }
}
