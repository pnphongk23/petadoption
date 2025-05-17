package com.projects.hanoipetadoption.ui.model

data class Pet(
    val id: String,
    val name: String,
    val breed: String,
    val age: String,
    val gender: PetGender,
    val description: String,
    val imageRes: String,
    val isFavorite: Boolean = false,
    val characteristics: List<String> = emptyList()
)

enum class PetGender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female")
}