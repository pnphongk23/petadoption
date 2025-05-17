package com.projects.hanoipetadoption.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity class for pets table with optimized indexing
 */
@Entity(
    tableName = "pets",
    indices = [
        Index(value = ["id"], unique = true)
    ]
)
data class PetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val breed: String,
    val age: String,
    val gender: String,
    val description: String,
    val imageRes: String,
    val isFavorite: Boolean = false,
    val weight: String? = null
) 