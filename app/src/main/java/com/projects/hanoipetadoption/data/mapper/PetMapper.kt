package com.projects.hanoipetadoption.data.mapper

import com.projects.hanoipetadoption.data.model.PetData
import com.projects.hanoipetadoption.domain.model.GenderDomain
import com.projects.hanoipetadoption.domain.model.PetDomain
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetCategory
import com.projects.hanoipetadoption.ui.model.PetGender

/**
 * Mappers to convert between different representations of the Pet entity
 */

// Convert data layer model to domain layer model
fun PetData.toDomain(): PetDomain {
    return PetDomain(
        id = id,
        name = name,
        breed = breed,
        gender = when (gender) {
            PetGender.MALE -> GenderDomain.MALE
            PetGender.FEMALE -> GenderDomain.FEMALE
            else -> GenderDomain.UNKNOWN
        },
        age = age,
        weight = weight,
        characteristics = characteristics,
        description = description,
        healthStatus = healthStatus,
        adoptionRequirements = adoptionRequirements,
        imageRes = imageRes,
        isFavorite = isFavorite
    )
}

// Convert UI layer model to domain layer model
fun Pet.toDomain(): PetDomain {
    return PetDomain(
        id = id,
        name = name,
        breed = breed,
        gender = when (gender) {
            PetGender.MALE -> GenderDomain.MALE
            PetGender.FEMALE -> GenderDomain.FEMALE
            else -> GenderDomain.UNKNOWN
        },
        age = age,
        weight = "Unknown", // We might need to add weight to the UI model
        characteristics = characteristics,
        description = description,
        healthStatus = mapOf(
            "Tiêm phòng" to false, 
            "Triệt sản" to true,
            "Tẩy giun" to true,
            "Kiểm tra sức khỏe tổng quát" to true
        ), // Default health statuses, replace with actual data when available
        adoptionRequirements = listOf(
            "Có không gian sống phù hợp",
            "Cam kết chăm sóc thú cưng đầy đủ",
            "Được phép nuôi thú cưng tại nơi ở",
            "Đồng ý kiểm tra định kỳ sau khi nhận nuôi"
        ), // Default adoption requirements, replace with actual data when available
        imageRes = imageRes,
        isFavorite = isFavorite
    )
}

// Convert domain layer model to UI model representation
fun PetDomain.toPresentation(): Pet {
    return Pet(
        id = id,
        name = name,
        breed = breed,
        gender = when (gender) {
            GenderDomain.MALE -> PetGender.MALE
            GenderDomain.FEMALE -> PetGender.FEMALE
            else -> PetGender.MALE
        },
        age = age,
        characteristics = characteristics,
        description = description,
        imageRes = imageRes,
        isFavorite = isFavorite,
        category = PetCategory.DOG // Default category, replace with actual data when available
    )
}
