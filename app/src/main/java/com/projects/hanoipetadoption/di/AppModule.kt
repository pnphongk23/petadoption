package com.projects.hanoipetadoption.di

import com.projects.hanoipetadoption.data.repository.PetRepositoryImpl
import com.projects.hanoipetadoption.data.source.PetLocalDataSource
import com.projects.hanoipetadoption.data.source.PetLocalDataSourceImpl
import com.projects.hanoipetadoption.domain.repository.PetRepository
import com.projects.hanoipetadoption.domain.usecase.GetPetByIdUseCase
import com.projects.hanoipetadoption.domain.usecase.GetPetsUseCase
import com.projects.hanoipetadoption.domain.usecase.TogglePetFavoriteUseCase
import com.projects.hanoipetadoption.presentation.viewmodel.PetDetailViewModel
import com.projects.hanoipetadoption.presentation.viewmodel.PetsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module that provides dependencies for the app
 */
val appModule = module {
    // Data Sources
    single<PetLocalDataSource> { PetLocalDataSourceImpl() }

    // Repositories
    single<PetRepository> { PetRepositoryImpl(get(), get()) }

    // Use Cases
    factory { GetPetByIdUseCase(get()) }
    factory { GetPetsUseCase(get()) }
    factory { TogglePetFavoriteUseCase(get()) }

    // ViewModels
    viewModel { PetDetailViewModel(get(), get()) }
    viewModel { PetsViewModel(get()) }
}
