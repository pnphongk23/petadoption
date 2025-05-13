package com.projects.hanoipetadoption.data.repository.postadoption

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.projects.hanoipetadoption.data.model.postadoption.CareCategory
import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.data.model.postadoption.DocumentType
import com.projects.hanoipetadoption.data.source.postadoption.CareLocalDataSourceImpl
import com.projects.hanoipetadoption.data.source.postadoption.CareRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class CareRepositoryImplTest {
    
    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var remoteDataSource: CareRemoteDataSource
    private lateinit var localDataSource: CareLocalDataSourceImpl
    private lateinit var repository: CareRepositoryImpl
    private lateinit var context: Context
    
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        remoteDataSource = mockk()
        localDataSource = mockk()
        context = mockk()
        
        repository = CareRepositoryImpl(remoteDataSource, localDataSource)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `getCareInstructionsForPet returns remote data successfully`() = runTest {
        // Given
        val petId = 1
        val sampleInstructions = listOf(
            CareInstructions(
                id = 1,
                petId = petId,
                title = "Test Care Instructions",
                description = "Test description",
                category = CareCategory.GENERAL,
                documentUrl = "https://example.com/test.pdf",
                documentType = DocumentType.PDF,
                createdDate = Date(),
                author = "Test Author"
            )
        )
        
        coEvery { remoteDataSource.getCareInstructionsForPet(petId) } returns sampleInstructions
        coEvery { localDataSource.saveCareInstructions(sampleInstructions) } returns Unit
        
        // When
        val result = repository.getCareInstructionsForPet(petId)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(sampleInstructions)
        
        coVerify { remoteDataSource.getCareInstructionsForPet(petId) }
        coVerify { localDataSource.saveCareInstructions(sampleInstructions) }
    }
    
    @Test
    fun `getCareInstructionsForPet returns cached data when remote fails`() = runTest {
        // Given
        val petId = 1
        val remoteException = RuntimeException("Network error")
        val cachedInstructions = listOf(
            CareInstructions(
                id = 1,
                petId = petId,
                title = "Cached Care Instructions",
                description = "Cached description",
                category = CareCategory.GENERAL,
                documentUrl = "https://example.com/cached.pdf",
                documentType = DocumentType.PDF,
                createdDate = Date(),
                author = "Cached Author"
            )
        )
        
        coEvery { remoteDataSource.getCareInstructionsForPet(petId) } throws remoteException
        coEvery { localDataSource.getCachedCareInstructionsForPet(petId) } returns cachedInstructions
        
        // When
        val result = repository.getCareInstructionsForPet(petId)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(cachedInstructions)
        
        coVerify { remoteDataSource.getCareInstructionsForPet(petId) }
        coVerify { localDataSource.getCachedCareInstructionsForPet(petId) }
    }
    
    @Test
    fun `getCareInstructionsForPet returns failure when both remote and local fail`() = runTest {
        // Given
        val petId = 1
        val remoteException = RuntimeException("Network error")
        val cacheException = RuntimeException("Cache error")
        
        coEvery { remoteDataSource.getCareInstructionsForPet(petId) } throws remoteException
        coEvery { localDataSource.getCachedCareInstructionsForPet(petId) } throws cacheException
        
        // When
        val result = repository.getCareInstructionsForPet(petId)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(remoteException)
        
        coVerify { remoteDataSource.getCareInstructionsForPet(petId) }
        coVerify { localDataSource.getCachedCareInstructionsForPet(petId) }
    }
    
    @Test
    fun `getCareInstructionById returns remote data successfully`() = runTest {
        // Given
        val instructionId = 1
        val sampleInstruction = CareInstructions(
            id = instructionId,
            petId = 1,
            title = "Test Care Instructions",
            description = "Test description",
            category = CareCategory.GENERAL,
            documentUrl = "https://example.com/test.pdf",
            documentType = DocumentType.PDF,
            createdDate = Date(),
            author = "Test Author"
        )
        
        coEvery { remoteDataSource.getCareInstructionById(instructionId) } returns sampleInstruction
        
        // When
        val result = repository.getCareInstructionById(instructionId)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(sampleInstruction)
        
        coVerify { remoteDataSource.getCareInstructionById(instructionId) }
    }
}
