package com.projects.hanoipetadoption.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.projects.hanoipetadoption.data.local.converter.TypeConverter
import com.projects.hanoipetadoption.data.local.dao.AdoptionDao
import com.projects.hanoipetadoption.data.local.dao.HealthRecordDao
import com.projects.hanoipetadoption.data.local.dao.PetAdoptionRequirementDao
import com.projects.hanoipetadoption.data.local.dao.PetCharacteristicDao
import com.projects.hanoipetadoption.data.local.dao.PetDao
import com.projects.hanoipetadoption.data.local.dao.PetHealthStatusDao
import com.projects.hanoipetadoption.data.local.dao.PetStatusUpdateDao
import com.projects.hanoipetadoption.data.local.dao.ReminderDao
import com.projects.hanoipetadoption.data.local.entity.HealthRecordEntity
import com.projects.hanoipetadoption.data.local.entity.HealthRecordMediaEntity
import com.projects.hanoipetadoption.data.local.entity.PetStatusUpdateEntity
import com.projects.hanoipetadoption.data.local.entity.PetStatusUpdateImageEntity
import com.projects.hanoipetadoption.data.local.entity.ReminderEntity
import com.projects.hanoipetadoption.data.model.AdoptionEntity
import com.projects.hanoipetadoption.data.model.PetAdoptionRequirementEntity
import com.projects.hanoipetadoption.data.model.PetCharacteristicEntity
import com.projects.hanoipetadoption.data.model.PetEntity
import com.projects.hanoipetadoption.data.model.PetHealthStatusEntity

@Database(
    entities = [
        AdoptionEntity::class,
        PetEntity::class,
        PetCharacteristicEntity::class,
        PetHealthStatusEntity::class,
        PetAdoptionRequirementEntity::class,
        HealthRecordEntity::class,
        PetStatusUpdateEntity::class,
        ReminderEntity::class,
        HealthRecordMediaEntity::class,
        PetStatusUpdateImageEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(TypeConverter::class)
abstract class PetAdoptionDatabase : RoomDatabase() {
    abstract fun adoptionDao(): AdoptionDao
    abstract fun petDao(): PetDao
    abstract fun petCharacteristicDao(): PetCharacteristicDao
    abstract fun petHealthStatusDao(): PetHealthStatusDao
    abstract fun petAdoptionRequirementDao(): PetAdoptionRequirementDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun petStatusUpdateDao(): PetStatusUpdateDao
    abstract fun reminderDao(): ReminderDao
}
