package com.projects.hanoipetadoption.util

import com.projects.hanoipetadoption.data.model.postadoption.CareCategory
import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.data.model.postadoption.DocumentType
import java.util.*

/**
 * Sample data for post-adoption care instructions
 */
object SampleCareInstructionsData {
    
    fun getCareInstructionsForPet(petId: String): List<CareInstructions> {
        val baseDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -30) // Sample data from a month ago
        }.time
        
        return listOf(
            CareInstructions(
                id = 101,
                petId = petId,
                title = "Chăm sóc thú cưng mới về nhà",
                description = "Hướng dẫn cơ bản cho các thú cưng mới được nhận nuôi.",
                category = CareCategory.GENERAL,
                documentUrl = "https://example.com/care/101.pdf",
                documentType = DocumentType.PDF,
                createdDate = baseDate,
                author = "Bác sĩ Hoàng Minh",
                isHighPriority = true,
                tags = listOf("thú cưng mới", "thích nghi", "cơ bản"),
                thumbnailUrl = "https://example.com/thumbnails/101.jpg"
            ),
            CareInstructions(
                id = 102,
                petId = petId,
                title = "Chế độ ăn cho chó con",
                description = "Hướng dẫn về chế độ ăn phù hợp cho chó con dưới 1 tuổi.",
                category = CareCategory.FEEDING,
                documentUrl = "https://example.com/care/102.pdf",
                documentType = DocumentType.PDF,
                createdDate = Date(baseDate.time + 86400000), // +1 day
                author = "Bác sĩ Nguyễn Văn A",
                tags = listOf("ăn uống", "chó con", "dinh dưỡng"),
                thumbnailUrl = "https://example.com/thumbnails/102.jpg"
            ),
            CareInstructions(
                id = 103,
                petId = petId,
                title = "Tiêm chủng định kỳ",
                description = "Thông tin về các loại vắc-xin cần thiết và lịch tiêm chủng.",
                category = CareCategory.HEALTHCARE,
                documentUrl = "https://example.com/care/103.pdf",
                documentType = DocumentType.PDF,
                createdDate = Date(baseDate.time + 172800000), // +2 days
                author = "Bác sĩ Thú y Trần Thị B",
                isHighPriority = true,
                tags = listOf("tiêm chủng", "vắc-xin", "sức khỏe"),
                thumbnailUrl = "https://example.com/thumbnails/103.jpg"
            ),
            CareInstructions(
                id = 104,
                petId = petId,
                title = "Hướng dẫn tắm cho mèo",
                description = "Video hướng dẫn cách tắm cho mèo an toàn và hiệu quả.",
                category = CareCategory.GROOMING,
                documentUrl = "https://example.com/care/104.mp4",
                documentType = DocumentType.VIDEO,
                createdDate = Date(baseDate.time + 259200000), // +3 days
                author = "Chuyên gia Lê Thị C",
                tags = listOf("tắm", "mèo", "vệ sinh"),
                thumbnailUrl = "https://example.com/thumbnails/104.jpg"
            ),
            CareInstructions(
                id = 105,
                petId = petId,
                title = "Huấn luyện chó cơ bản",
                description = "Các bài tập huấn luyện cơ bản cho chó mới nhận nuôi.",
                category = CareCategory.TRAINING,
                documentUrl = "https://example.com/care/105.pdf",
                documentType = DocumentType.PDF,
                createdDate = Date(baseDate.time + 345600000), // +4 days
                author = "Huấn luyện viên Phạm Văn D",
                tags = listOf("huấn luyện", "chó", "cơ bản"),
                thumbnailUrl = "https://example.com/thumbnails/105.jpg"
            )
        )
    }
    
    fun getCareInstructionById(instructionId: Int): CareInstructions? {
        return getAllCareInstructions().find { it.id == instructionId }
    }
    
    private fun getAllCareInstructions(): List<CareInstructions> {
        val allInstructions = mutableListOf<CareInstructions>()
        
        // Add instructions for multiple pets
//        for (petId in 1..5) {
//            allInstructions.addAll(getCareInstructionsForPet(petId))
//        }
        
        return allInstructions
    }
}
