package com.projects.hanoipetadoption.model

import com.google.common.truth.Truth.assertThat
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetCategory
import com.projects.hanoipetadoption.ui.model.PetGender
import com.projects.hanoipetadoption.util.TestData
import org.junit.Test

class PetModelTest {
    
    @Test
    fun `test pet object creation with required properties`() {
        val pet = Pet(
            id = "test-1",
            name = "Test Dog",
            category = PetCategory.DOG, 
            breed = "Test Breed",
            age = "1 year",
            gender = PetGender.MALE,
            description = "Test description",
            imageRes = "test_image"
        )
        
        assertThat(pet.id).isEqualTo("test-1")
        assertThat(pet.name).isEqualTo("Test Dog")
        assertThat(pet.category).isEqualTo(PetCategory.DOG)
        assertThat(pet.isFavorite).isFalse() // Default value check
        assertThat(pet.characteristics).isEmpty() // Default value check
    }
    
    @Test
    fun `test pet equality`() {
        val pet1 = Pet(
            id = "test-1",
            name = "Test Dog",
            category = PetCategory.DOG, 
            breed = "Test Breed",
            age = "1 year",
            gender = PetGender.MALE,
            description = "Test description",
            imageRes = "test_image"
        )
        
        val pet2 = Pet(
            id = "test-1",
            name = "Test Dog",
            category = PetCategory.DOG, 
            breed = "Test Breed",
            age = "1 year",
            gender = PetGender.MALE,
            description = "Test description",
            imageRes = "test_image"
        )
        
        val pet3 = Pet(
            id = "test-2", // Different ID
            name = "Test Dog",
            category = PetCategory.DOG, 
            breed = "Test Breed",
            age = "1 year",
            gender = PetGender.MALE,
            description = "Test description",
            imageRes = "test_image"
        )
        
        assertThat(pet1).isEqualTo(pet2)
        assertThat(pet1).isNotEqualTo(pet3)
    }
    
    @Test
    fun `test sample test data utility`() {
        val samplePets = TestData.samplePets
        
        assertThat(samplePets).isNotEmpty()
        assertThat(samplePets.size).isAtLeast(2)
        
        val pet1 = TestData.getPet("1")
        assertThat(pet1).isNotNull()
        assertThat(pet1?.name).isEqualTo("Max")
    }
}
