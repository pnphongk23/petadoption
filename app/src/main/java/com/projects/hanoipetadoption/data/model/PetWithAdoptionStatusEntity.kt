package com.projects.hanoipetadoption.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Entity class representing the result of a JOIN query between pets and adoption status
 * Returns a pet entity with its adoption status
 */
data class PetWithAdoptionStatusEntity(
    @Embedded val pet: PetEntity,
    val isAdopted: Boolean
) 