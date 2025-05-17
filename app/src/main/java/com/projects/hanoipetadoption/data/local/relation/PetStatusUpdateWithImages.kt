package com.projects.hanoipetadoption.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.projects.hanoipetadoption.data.local.entity.PetStatusUpdateEntity
import com.projects.hanoipetadoption.data.local.entity.PetStatusUpdateImageEntity

data class PetStatusUpdateWithImages(
    @Embedded val statusUpdate: PetStatusUpdateEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "status_update_id"
    )
    val images: List<PetStatusUpdateImageEntity>
) 