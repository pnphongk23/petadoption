package com.projects.hanoipetadoption.domain.usecase.postadoption

import android.content.Context
import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.domain.repository.postadoption.CareRepository

/**
 * Use case to open a care instruction document
 */
class OpenCareDocumentUseCase(private val repository: CareRepository) {
    /**
     * Execute the use case
     */
    suspend operator fun invoke(context: Context, instructions: CareInstructions): Result<Boolean> {
        return repository.openDocument(context, instructions)
    }
}
