package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions

/**
 * Interface for remote data source to fetch care instructions
 */
interface CareRemoteDataSource {
    /**
     * Get care instructions for a specific pet from the remote API
     * 
     * @param petId ID of the pet
     * @return List of care instructions
     */
    suspend fun getCareInstructionsForPet(petId: String): List<CareInstructions>
    
    /**
     * Get care instruction by ID from the remote API
     * 
     * @param instructionId ID of the instruction
     * @return The care instruction or null if not found
     */
    suspend fun getCareInstructionById(instructionId: Int): CareInstructions?
}
