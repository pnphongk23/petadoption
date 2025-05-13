package com.projects.hanoipetadoption.di

import com.projects.hanoipetadoption.data.network.ApiService
import com.projects.hanoipetadoption.data.repository.PetRepositoryImpl
import com.projects.hanoipetadoption.data.source.PetLocalDataSource
import com.projects.hanoipetadoption.data.source.PetLocalDataSourceImpl
import com.projects.hanoipetadoption.domain.repository.PetRepository
import com.projects.hanoipetadoption.domain.usecase.GetPetByIdUseCase
import com.projects.hanoipetadoption.domain.usecase.GetPetsUseCase
import com.projects.hanoipetadoption.domain.usecase.TogglePetFavoriteUseCase
import com.projects.hanoipetadoption.presentation.viewmodel.PetDetailViewModel
import com.projects.hanoipetadoption.presentation.viewmodel.PetsViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Koin module that provides dependencies for the app
 */
val appModule = module {
    // Network
    single {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://hanoipetadoption-api.example.com/") // Replace with actual API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
    
    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
    
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
