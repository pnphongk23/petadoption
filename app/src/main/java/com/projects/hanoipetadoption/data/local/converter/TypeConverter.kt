package com.projects.hanoipetadoption.data.local.converter

import androidx.room.TypeConverter
import com.projects.hanoipetadoption.data.model.postadoption.MediaType
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import com.projects.hanoipetadoption.data.model.postadoption.ReminderType
import java.util.Date

class TypeConverter {

    // Date Converters
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // ReminderType Converters
    @TypeConverter
    fun fromReminderType(value: String?): ReminderType? {
        return value?.let { ReminderType.valueOf(it) }
    }

    @TypeConverter
    fun reminderTypeToString(reminderType: ReminderType?): String? {
        return reminderType?.name
    }

    // RecordType Converters
    @TypeConverter
    fun fromRecordType(value: String?): RecordType? {
        return value?.let { RecordType.valueOf(it) }
    }

    @TypeConverter
    fun recordTypeToString(recordType: RecordType?): String? {
        return recordType?.name
    }

    // MediaType Converters
    @TypeConverter
    fun fromMediaType(value: String?): MediaType? {
        return value?.let { MediaType.valueOf(it) }
    }

    @TypeConverter
    fun mediaTypeToString(mediaType: MediaType?): String? {
        return mediaType?.name
    }
}
