package com.projects.hanoipetadoption.data.model.postadoption

fun RecordType.displayName(): String = when (this) {
    RecordType.GENERAL -> "Khám tổng quát"
    RecordType.VACCINATION -> "Tiêm phòng"
    RecordType.TREATMENT -> "Điều trị"
    RecordType.WEIGHT -> "Cân nặng"
} 