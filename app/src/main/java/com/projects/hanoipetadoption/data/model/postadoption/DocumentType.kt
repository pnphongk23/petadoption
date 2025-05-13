package com.projects.hanoipetadoption.data.model.postadoption

/**
 * Enum representing different types of care instruction documents
 */
enum class DocumentType {
    PDF,
    VIDEO,
    IMAGE,
    TEXT;

    fun getFileExtension(): String {
        return when (this) {
            PDF -> "pdf"
            VIDEO -> "mp4"
            IMAGE -> "jpg"
            TEXT -> "txt"
        }
    }

    fun getMimeType(): String {
        return when (this) {
            PDF -> "application/pdf"
            VIDEO -> "video/mp4"
            IMAGE -> "image/jpeg"
            TEXT -> "text/plain"
        }
    }
}
