package com.projects.hanoipetadoption.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.projects.hanoipetadoption.data.dao.AdoptionDao
import com.projects.hanoipetadoption.data.dao.PetAdoptionRequirementDao
import com.projects.hanoipetadoption.data.dao.PetCharacteristicDao
import com.projects.hanoipetadoption.data.dao.PetDao
import com.projects.hanoipetadoption.data.dao.PetHealthStatusDao
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
        PetAdoptionRequirementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PetAdoptionDatabase : RoomDatabase() {
    abstract fun adoptionDao(): AdoptionDao
    abstract fun petDao(): PetDao
    abstract fun petCharacteristicDao(): PetCharacteristicDao
    abstract fun petHealthStatusDao(): PetHealthStatusDao
    abstract fun petAdoptionRequirementDao(): PetAdoptionRequirementDao
}
