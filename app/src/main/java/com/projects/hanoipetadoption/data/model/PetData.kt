package com.projects.hanoipetadoption.data.model

import com.projects.hanoipetadoption.ui.model.PetGender

/**
 * Data layer representation of Pet entity
 */
data class PetData(
    val id: String,
    val name: String,
    val breed: String,
    val category: String,
    val gender: PetGender,
    val age: String,
    val weight: String,
    val characteristics: List<String>,
    val description: String,
    val healthStatus: Map<String, Boolean>,
    val adoptionRequirements: List<String>,
    val imageRes: String,
    val isFavorite: Boolean
)
