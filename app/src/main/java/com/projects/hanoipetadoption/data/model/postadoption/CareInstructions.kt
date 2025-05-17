package com.projects.hanoipetadoption.data.model.postadoption

import java.util.Date

/**
 * Model class for pet care instructions
 */
data class CareInstructions(
    val id: Int,
    val petId: String,
    val title: String,
    val description: String,
    val category: CareCategory,
    val documentUrl: String,
    val documentType: DocumentType,
    val createdDate: Date,
    val author: String,
    val isHighPriority: Boolean = false,
    val tags: List<String> = emptyList(),
    val thumbnailUrl: String? = null
)

/**
 * Enum representing different categories of care instructions
 */
enum class CareCategory {
    FEEDING,
    GROOMING,
    HEALTHCARE,
    TRAINING,
    EXERCISE,
    BEHAVIOR,
    GENERAL;

    val displayName: String
        get() = when (this) {
            FEEDING -> "Chế độ ăn"
            GROOMING -> "Vệ sinh"
            HEALTHCARE -> "Chăm sóc sức khỏe"
            TRAINING -> "Huấn luyện"
            EXERCISE -> "Vận động"
            BEHAVIOR -> "Hành vi"
            GENERAL -> "Chung"
        }
}
