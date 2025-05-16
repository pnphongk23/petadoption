package com.projects.hanoipetadoption.di

import androidx.room.Room
import com.projects.hanoipetadoption.data.database.PetAdoptionDatabase
import com.projects.hanoipetadoption.data.source.PetLocalDataSource
import com.projects.hanoipetadoption.data.source.PetLocalDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    // Database
    single {
        Room.databaseBuilder(
            androidApplication(),
            PetAdoptionDatabase::class.java,
            "pet_adoption_db"
        ).build()
    }
    
    // DAOs
    single { get<PetAdoptionDatabase>().adoptionDao() }
    single { get<PetAdoptionDatabase>().petDao() }
    single { get<PetAdoptionDatabase>().petCharacteristicDao() }
    single { get<PetAdoptionDatabase>().petHealthStatusDao() }
    single { get<PetAdoptionDatabase>().petAdoptionRequirementDao() }
    
    // Data Sources
    single<PetLocalDataSource> { 
        PetLocalDataSourceImpl(
            get(), // petDao
            get(), // characteristicDao
            get(), // healthStatusDao
            get()  // requirementDao
        ) 
    }
    
    // Data Initializer
    single { 
        // Get the local data source for initialization
        val dataSource = get<PetLocalDataSource>() as PetLocalDataSourceImpl
        // Make it available for initialization
        dataSource
    }
}