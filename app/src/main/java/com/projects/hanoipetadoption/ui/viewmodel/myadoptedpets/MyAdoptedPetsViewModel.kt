package com.projects.hanoipetadoption.ui.viewmodel.myadoptedpets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.hanoipetadoption.domain.usecase.GetAdoptedPetsUseCase
import com.projects.hanoipetadoption.ui.mapper.toUiModels
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetGender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * ViewModel for the MyAdoptedPetsList screen
 */
class MyAdoptedPetsViewModel(
    private val getAdoptedPetsUseCase: GetAdoptedPetsUseCase
) : ViewModel() {
    
    private val _adoptedPetsState = MutableStateFlow<AdoptedPetsState>(AdoptedPetsState.Loading)
    val adoptedPetsState: StateFlow<AdoptedPetsState> = _adoptedPetsState.asStateFlow()
    
    /**
     * Load the list of adopted pets for the current user
     */
    fun loadAdoptedPets() {
        viewModelScope.launch {
            _adoptedPetsState.value = AdoptedPetsState.Loading
            
            getAdoptedPetsUseCase().catch { exception ->
                _adoptedPetsState.value = AdoptedPetsState.Error("Không thể tải danh sách thú cưng: ${exception.message}")
            }.collect { result ->
                result.fold(
                    onSuccess = { petDomains ->
                        val pets = petDomains.toUiModels()
                        _adoptedPetsState.value = AdoptedPetsState.Success(pets)
                    },
                    onFailure = { error ->
                        _adoptedPetsState.value = AdoptedPetsState.Error("Không thể tải danh sách thú cưng: ${error.message}")
                    }
                )
            }
        }
    }
} 