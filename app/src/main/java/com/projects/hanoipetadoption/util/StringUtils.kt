package com.projects.hanoipetadoption.util

/**
 * Utility functions for string operations
 */
object StringUtils {
    
    /**
     * Removes Vietnamese diacritics from the input string
     * For example: "Chó Husky" becomes "Cho Husky"
     * 
     * @param input Text with possible Vietnamese characters
     * @return Text with Vietnamese diacritics removed
     */
    fun normalizeVietnamese(input: String): String {
        return input
            .replace("[àáạảãâầấậẩẫăằắặẳẵ]".toRegex(), "a")
            .replace("[èéẹẻẽêềếệểễ]".toRegex(), "e")
            .replace("[ìíịỉĩ]".toRegex(), "i")
            .replace("[òóọỏõôồốộổỗơờớợởỡ]".toRegex(), "o")
            .replace("[ùúụủũưừứựửữ]".toRegex(), "u")
            .replace("[ỳýỵỷỹ]".toRegex(), "y")
            .replace("[đ]".toRegex(), "d")
            .replace("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]".toRegex(), "A")
            .replace("[ÈÉẸẺẼÊỀẾỆỂỄ]".toRegex(), "E")
            .replace("[ÌÍỊỈĨ]".toRegex(), "I")
            .replace("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]".toRegex(), "O")
            .replace("[ÙÚỤỦŨƯỪỨỰỬỮ]".toRegex(), "U")
            .replace("[ỲÝỴỶỸ]".toRegex(), "Y")
            .replace("[Đ]".toRegex(), "D")
    }
    
    /**
     * Checks if a string contains another string regardless of Vietnamese diacritics
     * 
     * @param text Text to search in
     * @param query Query to search for
     * @param ignoreCase Whether to ignore case during search
     * @return true if text contains query regardless of diacritics
     */
    fun containsIgnoreDiacritics(text: String, query: String, ignoreCase: Boolean = true): Boolean {
        val normalizedText = normalizeVietnamese(text)
        val normalizedQuery = normalizeVietnamese(query)
        
        return normalizedText.contains(normalizedQuery, ignoreCase)
    }
}
