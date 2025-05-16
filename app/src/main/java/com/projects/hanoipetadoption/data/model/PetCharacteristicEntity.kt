package com.projects.hanoipetadoption.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity class for pet characteristics
 */
@Entity(
    tableName = "pet_characteristics",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("petId")]
)
data class PetCharacteristicEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: String,
    val characteristic: String
) 