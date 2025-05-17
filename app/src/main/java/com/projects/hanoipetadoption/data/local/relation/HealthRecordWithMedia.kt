package com.projects.hanoipetadoption.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.projects.hanoipetadoption.data.local.entity.HealthRecordEntity
import com.projects.hanoipetadoption.data.local.entity.HealthRecordMediaEntity

data class HealthRecordWithMedia(
    @Embedded val healthRecord: HealthRecordEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "health_record_id"
    )
    val mediaItems: List<HealthRecordMediaEntity>
) 