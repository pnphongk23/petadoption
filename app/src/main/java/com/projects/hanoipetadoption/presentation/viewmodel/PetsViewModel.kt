package com.projects.hanoipetadoption.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.data.mapper.toPresentation
import com.projects.hanoipetadoption.domain.usecase.GetPetsUseCase
import com.projects.hanoipetadoption.presentation.state.PetDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Pets Screen
 */
class PetsViewModel(
    private val getPetsUseCase: GetPetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PetDetailState>(PetDetailState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch {
            _uiState.value = PetDetailState.Loading
            
            getPetsUseCase()
                .onSuccess { pets ->
                    _uiState.value = PetDetailState.Success(pets.map { it.toPresentation() }.first())
                }
                .onFailure { error ->
                    _uiState.value = PetDetailState.Error(error.message ?: "Unknown error")
                }
        }
    }
}
