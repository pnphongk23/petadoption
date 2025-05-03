package com.projects.hanoipetadoption.domain.usecase

import com.projects.hanoipetadoption.domain.model.GenderDomain
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.domain.repository.PetRepository
import com.projects.hanoipetadoption.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPetsUseCaseTest {

    @MockK
    private lateinit var repository: PetRepository

    private lateinit var getPetsUseCase: GetPetsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getPetsUseCase = GetPetsUseCase(repository)
    }    @Test
    fun `invoke returns success with pet list when repository succeeds`() = runBlocking {
        // Given
        val petList = listOf(
            PetDomain(
                id = "1",
                name = "Max",
                breed = "Golden Retriever",
                gender = GenderDomain.MALE,
                age = "3 years",
                weight = "25 kg",
                description = "Friendly and energetic dog who loves to play fetch.",
                characteristics = listOf("Friendly", "Active", "Good with children"),
                healthStatus = mapOf("Vaccinated" to true, "Neutered" to false),
                adoptionRequirements = listOf("Spacious home", "Experience with dogs"),
                imageRes = "dog_golden_retriever",
                isFavorite = false
            )
        )
        coEvery { repository.getAllPets() } returns Result.success(petList)

        // When
        val result = getPetsUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(petList, result.getOrNull())
    }

    @Test
    fun `invoke returns failure when repository fails`() = runBlocking {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.getAllPets() } returns Result.failure(exception)

        // When
        val result = getPetsUseCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
