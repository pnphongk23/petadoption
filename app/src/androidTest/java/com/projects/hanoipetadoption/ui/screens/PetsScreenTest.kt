package com.projects.hanoipetadoption.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetCategory
import com.projects.hanoipetadoption.ui.model.PetGender
import com.projects.hanoipetadoption.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PetsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    
    @MockK
    lateinit var navController: NavController
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }
    
    @Test
    fun petsScreen_displaysAllPets() {
        // Given
        val pets = TestData.samplePets
        
        // When
        composeTestRule.setContent {
            PetsScreen(
                pets = pets,
                navigateToPetDetail = { },
                selectedCategory = null,
                onCategorySelected = { }
            )
        }
        
        // Then
        // Verify that pet names are displayed
        pets.forEach { pet ->
            composeTestRule.onNodeWithText(pet.name).assertIsDisplayed()
        }
    }
    
    @Test
    fun petsScreen_filtersByCategory() {
        // Given
        val allPets = listOf(
            Pet(
                id = "1",
                name = "Dog1",
                category = PetCategory.DOG,
                breed = "Breed1",
                age = "1 year",
                gender = PetGender.MALE,
                description = "Description",
                imageRes = "image"
            ),
            Pet(
                id = "2",
                name = "Cat1",
                category = PetCategory.CAT,
                breed = "Breed2",
                age = "2 years",
                gender = PetGender.FEMALE,
                description = "Description",
                imageRes = "image"
            )
        )
        
        // When
        composeTestRule.setContent {
            PetsScreen(
                pets = allPets,
                navigateToPetDetail = { },
                selectedCategory = PetCategory.DOG,
                onCategorySelected = { }
            )
        }
        
        // Then
        // Only dog should be visible, cat should not be visible
        composeTestRule.onNodeWithText("Dog1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cat1").assertDoesNotExist()
    }
    
    @Test
    fun petsScreen_navigatesToPetDetail_whenPetIsClicked() {
        // Given
        val pets = listOf(TestData.samplePets[0])
        val petId = "1"
        
        // When
        composeTestRule.setContent {
            PetsScreen(
                pets = pets,
                navigateToPetDetail = { clickedId -> 
                    // Verify that correct ID is passed
                    assert(clickedId == petId)
                },
                selectedCategory = null,
                onCategorySelected = { }
            )
        }
        
        // Then
        // Click on the pet
        composeTestRule.onNodeWithText("Max").performClick()
        // Navigation is verified in the lambda above
    }
}
