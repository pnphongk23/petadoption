package com.projects.hanoipetadoption.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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

class PetDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    
    @MockK
    lateinit var navController: NavController
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }
    
    @Test
    fun petDetailScreen_displaysAllPetDetails() {
        // Given
        val pet = TestData.samplePets[0] // Using the first sample pet
        
        // When
        composeTestRule.setContent {
            PetDetailScreen(
                pet = pet,
                onAdoptClick = { },
                onFavoriteToggle = { },
                onBackClick = { }
            )
        }
        
        // Then
        // Verify that pet details are displayed
        composeTestRule.onNodeWithText(pet.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(pet.breed).assertIsDisplayed()
        composeTestRule.onNodeWithText(pet.age).assertIsDisplayed()
        composeTestRule.onNodeWithText(pet.gender.displayName).assertIsDisplayed()
        composeTestRule.onNodeWithText(pet.description).assertIsDisplayed()
        
        // Verify characteristics are displayed
        pet.characteristics.forEach { characteristic ->
            composeTestRule.onNodeWithText(characteristic).assertIsDisplayed()
        }
        
        // Verify adopt button is displayed
        composeTestRule.onNodeWithText("Adopt").assertIsDisplayed()
    }
    
    @Test
    fun petDetailScreen_callsOnFavoriteToggle_whenFavoriteButtonClicked() {
        // Given
        val pet = TestData.samplePets[0]
        var favoriteToggled = false
        
        // When
        composeTestRule.setContent {
            PetDetailScreen(
                pet = pet,
                onAdoptClick = { },
                onFavoriteToggle = { favoriteToggled = true },
                onBackClick = { }
            )
        }
        
        // Then
        // Find and click the favorite button (assumed to have a content description)
        composeTestRule.onNodeWithContentDescription("Favorite Button").performClick()
        assert(favoriteToggled) { "onFavoriteToggle was not called when favorite button was clicked" }
    }
    
    @Test
    fun petDetailScreen_callsOnAdoptClick_whenAdoptButtonClicked() {
        // Given
        val pet = TestData.samplePets[0]
        var adoptClicked = false
        
        // When
        composeTestRule.setContent {
            PetDetailScreen(
                pet = pet,
                onAdoptClick = { adoptClicked = true },
                onFavoriteToggle = { },
                onBackClick = { }
            )
        }
        
        // Then
        // Find and click the adopt button
        composeTestRule.onNodeWithText("Adopt").performClick()
        assert(adoptClicked) { "onAdoptClick was not called when adopt button was clicked" }
    }
    
    @Test
    fun petDetailScreen_callsOnBackClick_whenBackButtonClicked() {
        // Given
        val pet = TestData.samplePets[0]
        var backClicked = false
        
        // When
        composeTestRule.setContent {
            PetDetailScreen(
                pet = pet,
                onAdoptClick = { },
                onFavoriteToggle = { },
                onBackClick = { backClicked = true }
            )
        }
        
        // Then
        // Find and click the back button (assumed to have a content description)
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(backClicked) { "onBackClick was not called when back button was clicked" }
    }
}
