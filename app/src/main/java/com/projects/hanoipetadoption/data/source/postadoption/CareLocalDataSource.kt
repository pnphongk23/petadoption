package com.projects.hanoipetadoption.data.source.postadoption

import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions

/**
 * Interface for local data source to fetch care instructions
 */
interface CareLocalDataSource {
    /**
     * Get care instructions for a specific pet from local storage
     * 
     * @param petId ID of the pet
     * @return List of care instructions
     */
    suspend fun getCareInstructionsForPet(petId: String): List<CareInstructions>
    
    /**
     * Get care instruction by ID from local storage
     * 
     * @param instructionId ID of the instruction
     * @return The care instruction or null if not found
     */
    suspend fun getCareInstructionById(instructionId: Int): CareInstructions?
    
    /**
     * Save care instructions to local storage
     * 
     * @param instructions List of care instructions to save
     */
    suspend fun saveCareInstructions(instructions: List<CareInstructions>)
}
