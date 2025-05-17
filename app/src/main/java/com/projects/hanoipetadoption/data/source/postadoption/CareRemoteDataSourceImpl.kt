package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.CareCategory
import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.data.model.postadoption.DocumentType
import com.projects.hanoipetadoption.data.network.PostAdoptionApiService
import java.util.Date

/**
 * Implementation of CareRemoteDataSource
 * Fetches care instructions data from the remote API
 */
class CareRemoteDataSourceImpl(
    private val apiService: PostAdoptionApiService
) : CareRemoteDataSource {    override suspend fun getCareInstructionsForPet(petId: String): List<CareInstructions> {
        try {
            // For development and testing, use sample data
            // In production, would use:
            // val response = apiService.getCareInstructionsForPet(petId)
            // return response.map { mapToCareInstructions(it) }
            
            return com.projects.hanoipetadoption.util.SampleCareInstructionsData.getCareInstructionsForPet(petId)
        } catch (e: Exception) {
            // Log the error
            throw e
        }
    }

    override suspend fun getCareInstructionById(instructionId: Int): CareInstructions? {
        try {
            // For development and testing, use sample data
            // In production, would use:
            // val response = apiService.getCareInstructionById(instructionId)
            // return mapToCareInstructions(response)
            
            return com.projects.hanoipetadoption.util.SampleCareInstructionsData.getCareInstructionById(instructionId)
        } catch (e: Exception) {
            // Log the error
            throw e
        }
    }
    
    /**
     * Maps API response data to CareInstructions model
     */
    private fun mapToCareInstructions(data: Map<String, Any>): CareInstructions {
        return CareInstructions(
            id = (data["id"] as Double).toInt(),
            petId = (data["pet_id"] as String),
            title = data["title"] as String,
            description = data["description"] as String,
            category = parseCategory(data["category"] as String),
            documentUrl = data["document_url"] as String,
            documentType = parseDocumentType(data["document_type"] as String),
            createdDate = parseDate(data["created_date"] as String),
            author = data["author"] as String,
            isHighPriority = data["is_high_priority"] as Boolean? ?: false,
            tags = (data["tags"] as List<*>?)?.filterIsInstance<String>() ?: emptyList(),
            thumbnailUrl = data["thumbnail_url"] as String?
        )
    }
    
    /**
     * Parse category string to CareCategory enum
     */
    private fun parseCategory(category: String): CareCategory {
        return try {
            CareCategory.valueOf(category.uppercase())
        } catch (e: Exception) {
            CareCategory.GENERAL
        }
    }
    
    /**
     * Parse document type string to DocumentType enum
     */
    private fun parseDocumentType(documentType: String): DocumentType {
        return try {
            DocumentType.valueOf(documentType.uppercase())
        } catch (e: Exception) {
            DocumentType.TEXT
        }
    }
    
    /**
     * Parse date string to Date object
     */
    private fun parseDate(dateString: String): Date {
        return try {
            // In a real implementation, would use a proper date parser
            Date()
        } catch (e: Exception) {
            Date()
        }
    }
}
