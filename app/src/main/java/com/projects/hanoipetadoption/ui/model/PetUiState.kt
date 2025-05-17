package com.projects.hanoipetadoption.ui.model

/**
 * Sealed class to represent the UI state for pets screens
 */
sealed class PetsUiState {
    /**
     * Loading state - shown when data is being fetched
     */
    object Loading : PetsUiState()
    
    /**
     * Success state - shown when data has been successfully fetched
     */
    data class Success(val pets: List<PetWithAdoptionStatus>) : PetsUiState()
    
    /**
     * Error state - shown when an error occurred during data fetching
     */
    data class Error(val message: String) : PetsUiState()
} 