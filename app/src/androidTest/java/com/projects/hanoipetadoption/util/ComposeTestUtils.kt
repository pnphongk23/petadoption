package com.projects.hanoipetadoption.util

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetCategory
import com.projects.hanoipetadoption.ui.model.PetGender

/**
 * Test utility class for UI tests
 */
class ComposeTestUtils {
    /**
     * Fills out the adoption form with valid data
     */
    fun fillAdoptionForm(composeRule: ComposeContentTestRule) {
        // Fill name field
        composeRule.onNodeWithText("Full Name").performTextInput("John Doe")
        
        // Fill email field  
        composeRule.onNodeWithText("Email").performTextInput("john.doe@example.com")
        
        // Fill phone field
        composeRule.onNodeWithText("Phone").performTextInput("1234567890")
        
        // Fill address field
        composeRule.onNodeWithText("Address").performTextInput("123 Main St, Hanoi, Vietnam")
        
        // Fill additional information field
        composeRule.onNodeWithText("Additional Information").performTextInput("I have experience with pets and a spacious home.")
    }
    
    companion object {
        /**
         * Creates a sample pet for testing
         */
        fun createSamplePet() = Pet(
            id = "test-1",
            name = "Buddy",
            category = PetCategory.DOG,
            breed = "Mixed Breed",
            age = "2 years",
            gender = PetGender.MALE,
            description = "Friendly and playful dog looking for a loving home.",
            imageRes = "test_dog_image"
        )
    }
}
