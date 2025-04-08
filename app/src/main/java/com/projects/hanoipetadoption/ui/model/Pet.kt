package com.projects.hanoipetadoption.ui.model

data class Pet(
    val id: String,
    val name: String,
    val category: PetCategory,
    val breed: String,
    val age: String,
    val gender: PetGender,
    val description: String,
    val imageRes: String,
    val isFavorite: Boolean = false,
    val characteristics: List<String> = emptyList()
)

enum class PetCategory(val displayName: String) {
    DOG("Dog"),
    CAT("Cat"),
    BIRD("Bird"),
    SMALL_ANIMAL("Small Animal"),
    OTHER("Other")
}

enum class PetGender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female")
}