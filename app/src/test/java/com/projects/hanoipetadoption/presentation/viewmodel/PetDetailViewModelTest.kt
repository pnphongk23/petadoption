package com.projects.hanoipetadoption.presentation.viewmodel

import com.google.common.truth.Truth.assertThat
import com.projects.hanoipetadoption.data.mapper.toDomain
import com.projects.hanoipetadoption.domain.usecase.GetPetByIdUseCase
import com.projects.hanoipetadoption.domain.usecase.TogglePetFavoriteUseCase
import com.projects.hanoipetadoption.ui.state.PetDetailState
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetCategory
import com.projects.hanoipetadoption.ui.model.PetGender
import com.projects.hanoipetadoption.ui.viewmodel.PetDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.Result

@OptIn(ExperimentalCoroutinesApi::class)
class PetDetailViewModelTest {
    
    private lateinit var viewModel: PetDetailViewModel
    private lateinit var getPetByIdUseCase: GetPetByIdUseCase
    private lateinit var togglePetFavoriteUseCase: TogglePetFavoriteUseCase
    
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getPetByIdUseCase = mockk()
        togglePetFavoriteUseCase = mockk()
        viewModel = PetDetailViewModel(getPetByIdUseCase, togglePetFavoriteUseCase)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `loadPetDetails should update state to Loading then Success when use case returns success`() = runTest {
        // Given
        val petId = "1"
        val petDomain = Pet(
            id = petId,
            name = "Max",
            category = PetCategory.DOG,
            breed = "Golden Retriever",
            age = "3 years",
            gender = PetGender.MALE,
            description = "Friendly and energetic dog who loves to play fetch.",
            imageRes = "dog_golden_retriever",
            isFavorite = false,
            characteristics = listOf("Friendly", "Active", "Good with children")
        ).toDomain()
        
        coEvery { getPetByIdUseCase(petId) } returns Result.success(petDomain)
        
        // Create a list to collect states
        val states = mutableListOf<PetDetailState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }
        
        // When
        viewModel.loadPetDetails(petId)
        testScheduler.advanceUntilIdle()
        
        // Then
        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(PetDetailState.Loading::class.java)
        assertThat(states[1]).isInstanceOf(PetDetailState.Success::class.java)
        
        val successState = states[1] as PetDetailState.Success
        assertThat(successState.pet.id).isEqualTo(petId)
        assertThat(successState.pet.name).isEqualTo("Max")
        
        job.cancel()
    }
    
    @Test
    fun `loadPetDetails should update state to Loading then Error when use case returns failure`() = runTest {
        // Given
        val petId = "999"
        val errorMessage = "Pet not found"
        coEvery { getPetByIdUseCase(petId) } returns Result.failure(Exception(errorMessage))
        
        // Create a list to collect states
        val states = mutableListOf<PetDetailState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }
        
        // When
        viewModel.loadPetDetails(petId)
        testScheduler.advanceUntilIdle()
        
        // Then
        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(PetDetailState.Loading::class.java)
        assertThat(states[1]).isInstanceOf(PetDetailState.Error::class.java)
        
        val errorState = states[1] as PetDetailState.Error
        assertThat(errorState.message).isEqualTo(errorMessage)
        
        job.cancel()
    }
    
    @Test
    fun `toggleFavorite should call togglePetFavoriteUseCase and reload pet details on success`() = runTest {
        // Given
        val petId = "1"
        coEvery { togglePetFavoriteUseCase(petId) } returns Result.success(true)
        coEvery { getPetByIdUseCase(petId) } returns Result.success(
            Pet(
                id = petId,
                name = "Max",
                category = PetCategory.DOG,
                breed = "Golden Retriever",
                age = "3 years",
                gender = PetGender.MALE,
                description = "Test description",
                imageRes = "test_image",
                isFavorite = true, // Now it's a favorite
                characteristics = emptyList()
            ).toDomain()
        )
        
        // Create a list to collect states
        val states = mutableListOf<PetDetailState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }
        
        // When
        viewModel.toggleFavorite(petId)
        testScheduler.advanceUntilIdle()
        
        // Then
        // Verify that loadPetDetails was called (which sets Loading then Success states)
        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(PetDetailState.Loading::class.java)
        assertThat(states[1]).isInstanceOf(PetDetailState.Success::class.java)
        
        val successState = states[1] as PetDetailState.Success
        assertThat(successState.pet.id).isEqualTo(petId)
        assertThat(successState.pet.isFavorite).isTrue() // Verify the pet is now a favorite
        
        job.cancel()
    }
}
