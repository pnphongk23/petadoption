package com.projects.hanoipetadoption.ui.model

data class AdoptionApplication(
    val petId: String,
    val petName: String,
    val applicantName: String,
    val phoneNumber: String,
    val email: String,
    val address: String,
    val petExperience: String,
    val livingSpace: String,
    val hoursAtHome: String,
    val applicationStatus: ApplicationStatus = ApplicationStatus.PENDING
)

enum class ApplicationStatus {
    PENDING,
    UNDER_REVIEW,
    INTERVIEW_SCHEDULED,
    APPROVED,
    REJECTED;
    
    val displayName: String
        get() = when (this) {
            PENDING -> "Đang chờ xử lý"
            UNDER_REVIEW -> "Đang xem xét"
            INTERVIEW_SCHEDULED -> "Đã lên lịch phỏng vấn"
            APPROVED -> "Đã được chấp nhận"
            REJECTED -> "Không được chấp nhận"
        }
}

enum class LivingSpaceType(val displayName: String) {
    HOUSE("Nhà riêng"),
    APARTMENT("Chung cư"),
    OTHER("Khác")
}
