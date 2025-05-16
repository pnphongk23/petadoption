package com.projects.hanoipetadoption.ui.state

import com.projects.hanoipetadoption.ui.model.Pet

/**
 * Sealed class to represent different states of the Pet Detail Screen
 */
sealed class PetDetailState {
    data object Loading : PetDetailState()
    data class Success(val pet: Pet) : PetDetailState()
    data class Error(val message: String) : PetDetailState()
}
