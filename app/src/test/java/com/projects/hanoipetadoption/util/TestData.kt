package com.projects.hanoipetadoption.util

import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetCategory
import com.projects.hanoipetadoption.ui.model.PetGender

/**
 * Provides test data for unit tests
 */
object TestData {
    // Sample pets for testing
    val samplePets = listOf(
        Pet(
            id = "1",
            name = "Max",
            category = PetCategory.DOG,
            breed = "Golden Retriever",
            age = "3 years",
            gender = PetGender.MALE,
            description = "Friendly and energetic dog who loves to play fetch.",
            imageRes = "dog_golden_retriever",
            characteristics = listOf("Friendly", "Active", "Good with children")
        ),
        Pet(
            id = "2",
            name = "Luna",
            category = PetCategory.CAT,
            breed = "Siamese",
            age = "2 years",
            gender = PetGender.FEMALE,
            description = "Calm and affectionate cat who enjoys quiet companionship.",
            imageRes = "cat_siamese",
            isFavorite = true,
            characteristics = listOf("Quiet", "Independent", "Affectionate")
        ),
        Pet(
            id = "3",
            name = "Charlie",
            category = PetCategory.DOG,
            breed = "Beagle",
            age = "1 year",
            gender = PetGender.MALE,
            description = "Playful puppy with lots of energy.",
            imageRes = "dog_beagle",
            characteristics = listOf("Playful", "Curious", "Friendly")
        )
    )

    // Get a single pet by ID
    fun getPet(id: String) = samplePets.find { it.id == id }
}
