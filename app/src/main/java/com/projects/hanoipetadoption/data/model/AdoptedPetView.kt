package com.projects.hanoipetadoption.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * View class that represents the joined result of a pet and its adoption information
 */
data class AdoptedPetView(
    @Embedded val pet: PetEntity,
    val adoptionId: String,
    val applicationDate: Long
) 