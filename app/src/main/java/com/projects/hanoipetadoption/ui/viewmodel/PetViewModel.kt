package com.projects.hanoipetadoption.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.domain.repository.PetRepository
import com.projects.hanoipetadoption.data.repository.AdoptionRepository
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.ui.model.AdoptionApplication
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetWithAdoptionStatus
import com.projects.hanoipetadoption.data.mapper.toPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetViewModel(
    private val petRepository: PetRepository,
    private val adoptionRepository: AdoptionRepository
) : ViewModel() {

    private val _allPetsFromSource = MutableStateFlow<List<Pet>>(emptyList())

    init {
        loadPets()
    }

    private fun loadPets() {
        viewModelScope.launch {
            try {
                val result = petRepository.getAllPets()
                result.onSuccess { petDomainList ->
                    _allPetsFromSource.value = petDomainList.map { it.toPresentation() }
                }
            } catch (e: Exception) {
                // Handle error - you might want to add error handling here
                // For example, update a UI state or log the error
            }
        }
    }

    val petsWithAdoptionStatus: StateFlow<List<PetWithAdoptionStatus>> =
        combine(_allPetsFromSource, adoptionRepository.getAdoptedPetIds()) { pets, adoptedIds ->
            pets.map { pet ->
                PetWithAdoptionStatus(pet, adoptedIds.contains(pet.id))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adoptedPets: StateFlow<List<PetWithAdoptionStatus>> =
        petsWithAdoptionStatus.map { list ->
            list.filter { it.isAdopted }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun submitAdoptionApplication(application: AdoptionApplication) {
        viewModelScope.launch {
            adoptionRepository.submitApplication(application)
        }
    }
    
    // Function to refresh the pets list
    fun refreshPets() {
        loadPets()
    }
}
