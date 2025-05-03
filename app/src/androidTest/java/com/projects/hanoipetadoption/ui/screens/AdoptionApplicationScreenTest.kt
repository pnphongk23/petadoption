package com.projects.hanoipetadoption.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.projects.hanoipetadoption.ui.model.AdoptionApplication
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.util.ComposeTestUtils
import com.projects.hanoipetadoption.util.TestData
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AdoptionApplicationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    
    private val testUtils = ComposeTestUtils()
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }
    
    @Test
    fun adoptionApplicationScreen_displaysCorrectPetInfo() {
        // Given
        val pet = TestData.samplePets[0] // Using the first sample pet
        
        // When
        composeTestRule.setContent {
            AdoptionApplicationScreen(
                pet = pet,
                onSubmitApplication = { },
                onBackClick = { }
            )
        }
        
        // Then
        // Verify that pet name is displayed
        composeTestRule.onNodeWithText("Adopt ${pet.name}").assertIsDisplayed()
        // Verify form fields are displayed
        composeTestRule.onNodeWithText("Full Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Phone").assertIsDisplayed()
        composeTestRule.onNodeWithText("Address").assertIsDisplayed()
        composeTestRule.onNodeWithText("Additional Information").assertIsDisplayed()
        // Verify submit button is displayed
        composeTestRule.onNodeWithText("Submit Application").assertIsDisplayed()
    }
    
    @Test
    fun adoptionApplicationScreen_enablesSubmitButton_whenFormIsValid() {
        // Given
        val pet = TestData.samplePets[0]
        
        // When
        composeTestRule.setContent {
            AdoptionApplicationScreen(
                pet = pet,
                onSubmitApplication = { },
                onBackClick = { }
            )
        }
        
        // Submit button should be disabled initially
        composeTestRule.onNodeWithText("Submit Application")
            .assertIsDisplayed()
            .assertIsNotEnabled()
        
        // Fill out all required fields
        testUtils.fillAdoptionForm(composeTestRule)
        
        // Then
        // Submit button should be enabled
        composeTestRule.onNodeWithText("Submit Application")
            .assertIsEnabled()
    }
    
    @Test
    fun adoptionApplicationScreen_callsOnSubmitApplication_whenFormIsSubmitted() {
        // Given
        val pet = TestData.samplePets[0]
        var submittedApplication: AdoptionApplication? = null
        
        // When
        composeTestRule.setContent {
            AdoptionApplicationScreen(
                pet = pet,
                onSubmitApplication = { application -> 
                    submittedApplication = application
                },
                onBackClick = { }
            )
        }
        
        // Fill out all required fields
        testUtils.fillAdoptionForm(composeTestRule)
        
        // Click submit button
        composeTestRule.onNodeWithText("Submit Application").performClick()
        
        // Then
        // Verify application was submitted with correct data
        assert(submittedApplication != null) { "onSubmitApplication was not called with application data" }
        submittedApplication?.let { application ->
            assert(application.petId == pet.id) { "Pet ID does not match" }
            assert(application.applicantName == "John Doe") { "Applicant name does not match" }
            assert(application.applicantEmail == "john.doe@example.com") { "Applicant email does not match" }
            assert(application.applicantPhone == "1234567890") { "Applicant phone does not match" }
            assert(application.applicantAddress == "123 Main St, Hanoi, Vietnam") { "Applicant address does not match" }
        }
    }
    
    @Test
    fun adoptionApplicationScreen_showsValidationErrors_whenFieldsAreInvalid() {
        // Given
        val pet = TestData.samplePets[0]
        
        // When
        composeTestRule.setContent {
            AdoptionApplicationScreen(
                pet = pet,
                onSubmitApplication = { },
                onBackClick = { }
            )
        }
        
        // Fill with invalid email
        composeTestRule.onNodeWithText("Full Name").performTextInput("John Doe")
        composeTestRule.onNodeWithText("Email").performTextInput("invalid-email")
        composeTestRule.onNodeWithText("Phone").performTextInput("1234567890")
        composeTestRule.onNodeWithText("Address").performTextInput("123 Main St, Hanoi, Vietnam")
        
        // Then
        // Check for validation error message
        composeTestRule.onNodeWithText("Please enter a valid email address").assertIsDisplayed()
        
        // Submit button should remain disabled
        composeTestRule.onNodeWithText("Submit Application").assertIsNotEnabled()
    }
}
