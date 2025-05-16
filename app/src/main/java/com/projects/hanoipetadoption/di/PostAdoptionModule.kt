package com.projects.hanoipetadoption.di

import android.content.Context
import androidx.work.WorkManager

import com.projects.hanoipetadoption.data.network.PostAdoptionApiService
import com.projects.hanoipetadoption.data.repository.postadoption.CareRepositoryImpl
import com.projects.hanoipetadoption.data.repository.postadoption.HealthRecordRepositoryImpl
import com.projects.hanoipetadoption.data.repository.postadoption.HealthRepositoryImpl
import com.projects.hanoipetadoption.data.repository.postadoption.ReminderRepositoryImpl
import com.projects.hanoipetadoption.data.repository.postadoption.StatusRepositoryImpl
import com.projects.hanoipetadoption.data.source.postadoption.CareLocalDataSource
import com.projects.hanoipetadoption.data.source.postadoption.CareLocalDataSourceImpl
import com.projects.hanoipetadoption.data.source.postadoption.CareRemoteDataSource
import com.projects.hanoipetadoption.data.source.postadoption.CareRemoteDataSourceImpl
import com.projects.hanoipetadoption.data.source.postadoption.HealthLocalDataSource
import com.projects.hanoipetadoption.data.source.postadoption.HealthLocalDataSourceImpl
import com.projects.hanoipetadoption.data.source.postadoption.HealthRecordRemoteDataSource
import com.projects.hanoipetadoption.data.source.postadoption.HealthRecordRemoteDataSourceImpl
import com.projects.hanoipetadoption.data.source.postadoption.ReminderLocalDataSource
import com.projects.hanoipetadoption.data.source.postadoption.ReminderLocalDataSourceImpl
import com.projects.hanoipetadoption.data.source.postadoption.ReminderRemoteDataSource
import com.projects.hanoipetadoption.data.source.postadoption.ReminderRemoteDataSourceImpl
import com.projects.hanoipetadoption.data.source.postadoption.StatusLocalDataSource
import com.projects.hanoipetadoption.data.source.postadoption.StatusLocalDataSourceImpl
import com.projects.hanoipetadoption.data.source.postadoption.StatusRemoteDataSource
import com.projects.hanoipetadoption.data.source.postadoption.StatusRemoteDataSourceImpl
import com.projects.hanoipetadoption.domain.repository.postadoption.CareRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.HealthRecordRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.HealthRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.ReminderRepository
import com.projects.hanoipetadoption.domain.repository.postadoption.StatusRepository
import com.projects.hanoipetadoption.domain.usecase.postadoption.AddMediaToHealthRecordUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.AddPetStatusUpdateUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.CreateHealthRecordUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.CreateVaccinationReminderUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetCareInstructionsUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetHealthRecordsForPetUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetPetStatusUpdatesUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetRemindersForPetUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.GetUpcomingRemindersUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.MarkReminderCompleteUseCase
import com.projects.hanoipetadoption.domain.usecase.postadoption.OpenCareDocumentUseCase
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.CareInstructionsViewModel
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.HealthTrackerViewModel
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.PetStatusViewModel
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.ReminderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Koin module for post-adoption tracking features
 */
val postAdoptionModule = module {
    // WorkManager for scheduling notifications
    single { WorkManager.getInstance(get<Context>()) }
    
    // API Service
    single<PostAdoptionApiService> {
        get<Retrofit>().create(PostAdoptionApiService::class.java)    }
    
    // Data Sources
    single<HealthRecordRemoteDataSource> { 
        HealthRecordRemoteDataSourceImpl(get())
    }
    single<HealthLocalDataSource> {
        HealthLocalDataSourceImpl(get())
    }
    single<ReminderRemoteDataSource> {
        ReminderRemoteDataSourceImpl(get())
    }
    single<ReminderLocalDataSource> {
        ReminderLocalDataSourceImpl(get(), get())
    }
    single<CareRemoteDataSource> {
        CareRemoteDataSourceImpl(get())
    }
    single<CareLocalDataSource> {
        CareLocalDataSourceImpl(get())
    }
    single<StatusRemoteDataSource> {
        StatusRemoteDataSourceImpl(get())
    }
    single<StatusLocalDataSource> {
        StatusLocalDataSourceImpl(get())
    }
    
    // Repositories
    single<HealthRepository> { HealthRepositoryImpl(get(), get()) }
    single<HealthRecordRepository> { HealthRecordRepositoryImpl(get()) }
    single<ReminderRepository> { ReminderRepositoryImpl(get(), get()) }
    single<CareRepository> { CareRepositoryImpl(get(), get()) }
    single<StatusRepository> { StatusRepositoryImpl(get(), get()) }

    // Use Cases
    factory { GetHealthRecordsForPetUseCase(get()) }
    factory { CreateHealthRecordUseCase(get()) }
    factory { AddMediaToHealthRecordUseCase(get()) }
    factory { GetUpcomingRemindersUseCase(get()) }
    factory { GetRemindersForPetUseCase(get()) }
    factory { CreateVaccinationReminderUseCase(get()) }
    factory { MarkReminderCompleteUseCase(get()) }
    factory { GetCareInstructionsUseCase(get()) }
    factory { OpenCareDocumentUseCase(get()) }
    factory { GetPetStatusUpdatesUseCase(get()) }
    factory { AddPetStatusUpdateUseCase(get()) }

    // ViewModels
    viewModel { HealthTrackerViewModel(get(), get(), get()) }
    viewModel { ReminderViewModel(get(), get(), get(), get()) }
    viewModel { CareInstructionsViewModel(get(), get()) }
    viewModel { PetStatusViewModel(get(), get()) }
}
