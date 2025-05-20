package com.projects.hanoipetadoption.data.model.postadoption

fun ReminderType.displayName(): String = when (this) {
    ReminderType.VACCINATION -> "Tiêm phòng"
    ReminderType.MEDICATION -> "Uống thuốc"
    ReminderType.CHECKUP -> "Khám sức khoẻ"
    ReminderType.GROOMING -> "Chải lông/Tắm rửa"
    ReminderType.OTHER -> "Khác"
} 