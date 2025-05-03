package com.projects.hanoipetadoption.model

import com.google.common.truth.Truth.assertThat
import com.projects.hanoipetadoption.ui.model.PetCategory
import com.projects.hanoipetadoption.ui.model.PetGender
import org.junit.Test

class PetEnumsTest {
    
    @Test
    fun `test PetCategory enum values and display names`() {
        // Test all enum values exist
        val categories = PetCategory.values()
        assertThat(categories).hasLength(5)
        
        // Test specific enum values
        assertThat(PetCategory.DOG.displayName).isEqualTo("Dog")
        assertThat(PetCategory.CAT.displayName).isEqualTo("Cat")
        assertThat(PetCategory.BIRD.displayName).isEqualTo("Bird")
        assertThat(PetCategory.SMALL_ANIMAL.displayName).isEqualTo("Small Animal")
        assertThat(PetCategory.OTHER.displayName).isEqualTo("Other")
    }
    
    @Test
    fun `test PetGender enum values and display names`() {
        // Test all enum values exist
        val genders = PetGender.values()
        assertThat(genders).hasLength(2)
        
        // Test specific enum values
        assertThat(PetGender.MALE.displayName).isEqualTo("Male")
        assertThat(PetGender.FEMALE.displayName).isEqualTo("Female")
    }
}
