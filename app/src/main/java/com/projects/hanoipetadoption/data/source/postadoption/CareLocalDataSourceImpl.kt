package com.projects.hanoipetadoption.data.source.postadoption

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.data.model.postadoption.DocumentType
import com.projects.hanoipetadoption.data.network.PostAdoptionApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.File
import java.io.IOException

/**
 * Implementation of CareLocalDataSource
 * Manages local storage of care instructions and document handling
 */
class CareLocalDataSourceImpl(
    private val apiService: PostAdoptionApiService
) : CareLocalDataSource {
    
    // In-memory cache of care instructions
    private val careInstructionsCache = mutableMapOf<String, List<CareInstructions>>()
    
    override suspend fun getCareInstructionsForPet(petId: String): List<CareInstructions> {
        // In a real implementation, this would fetch from Room database
        // For now, return from the cache or empty list if not found
        return careInstructionsCache[petId] ?: emptyList()
    }
    
    override suspend fun getCareInstructionById(instructionId: Int): CareInstructions? {
        // Flatten all cached instructions and find the one with matching ID
        // In a real implementation, this would query the Room database directly
        return careInstructionsCache.values
            .flatten()
            .find { it.id == instructionId }
    }
    
    override suspend fun saveCareInstructions(instructions: List<CareInstructions>) {
        // Group instructions by petId and store in cache
        // In a real implementation, this would save to Room database
        instructions.groupBy { it.petId }.forEach { (petId, petInstructions) ->
            careInstructionsCache[petId] = petInstructions
        }
    }
    
    /**
     * Get cached care instructions for a specific pet
     * This is a special method used by the repository for fallback
     */
    suspend fun getCachedCareInstructionsForPet(petId: String): List<CareInstructions> {
        return getCareInstructionsForPet(petId)
    }
      /**
     * Open a care instruction document using the appropriate Intent
     * Downloads the document if not available locally
     */
    suspend fun openDocument(context: Context, instructions: CareInstructions): Boolean {
        try {
            // Check if the document is already downloaded
            var documentFile = getLocalDocumentFile(context, instructions)
            
            if (documentFile != null && documentFile.exists()) {
                // File exists locally, open it
                openLocalDocument(context, documentFile, instructions.documentType)
                return true
            } else {
                // Document not found locally, try to download it
                documentFile = downloadDocument(context, instructions)
                
                if (documentFile != null && documentFile.exists()) {
                    openLocalDocument(context, documentFile, instructions.documentType)
                    return true
                }
                return false
            }
        } catch (e: Exception) {
            // Log the error
            return false
        }
    }
    
    /**
     * Download a document from the remote API and save it locally
     */    private suspend fun downloadDocument(context: Context, instructions: CareInstructions): File? = withContext(Dispatchers.IO) {
        try {
            val response = apiService.downloadCareInstructionDocument(instructions.id)
            
            val filename = "care_${instructions.id}.${instructions.documentType.getFileExtension()}"
            val documentsDir = File(context.filesDir, "care_documents")
            if (!documentsDir.exists()) {
                documentsDir.mkdirs()
            }
            
            val file = File(documentsDir, filename)
            
            // Write the response body to file
            BufferedInputStream(response.byteStream()).use { inputStream ->
                BufferedOutputStream(FileOutputStream(file)).use { outputStream ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
            
            return@withContext file
        } catch (e: Exception) {
            // Log the error
            return@withContext null
        }
    }
    
    /**
     * Get the local file for a document if it exists
     */
    private fun getLocalDocumentFile(context: Context, instructions: CareInstructions): File? {
        val filename = "care_${instructions.id}.${instructions.documentType.getFileExtension()}"
        val documentsDir = File(context.filesDir, "care_documents")
        if (!documentsDir.exists()) {
            documentsDir.mkdirs()
        }
        
        val file = File(documentsDir, filename)
        return if (file.exists()) file else null
    }
    
    /**
     * Open a local document file with the appropriate intent
     */
    private fun openLocalDocument(context: Context, file: File, documentType: DocumentType) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, documentType.getMimeType())
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        context.startActivity(Intent.createChooser(intent, "Open Document"))
    }
}
