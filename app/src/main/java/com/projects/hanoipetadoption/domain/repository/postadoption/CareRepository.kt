package com.projects.hanoipetadoption.domain.repository.postadoption

import android.content.Context
import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.data.model.postadoption.DocumentType

/**
 * Repository interface for pet care instructions
 */
interface CareRepository {
    /**
     * Get care instructions for a specific pet
     * 
     * @param petId ID of the pet
     * @return List of care instructions
     */
    suspend fun getCareInstructionsForPet(petId: String): Result<List<CareInstructions>>

    /**
     * Get care instruction by ID
     * 
     * @param instructionId ID of the instruction
     * @return The care instruction or null if not found
     */
    suspend fun getCareInstructionById(instructionId: Int): Result<CareInstructions?>

    /**
     * Open a care instruction document
     * 
     * @param context Android context
     * @param instructions The care instruction to open
     * @return Whether opening the document was successful
     */
    suspend fun openDocument(context: Context, instructions: CareInstructions): Result<Boolean>
}
