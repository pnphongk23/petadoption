package com.projects.hanoipetadoption.ui.viewmodel.myadoptedpets

import com.projects.hanoipetadoption.ui.model.Pet

/**
 * State for the adopted pets list screen
 */
sealed class AdoptedPetsState {
    /**
     * Loading state when pets are being fetched
     */
    object Loading : AdoptedPetsState()
    
    /**
     * Success state when pets have been loaded
     */
    data class Success(val pets: List<Pet>) : AdoptedPetsState()
    
    /**
     * Error state when there's an issue loading pets
     */
    data class Error(val message: String) : AdoptedPetsState()
} 