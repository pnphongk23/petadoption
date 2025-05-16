package com.projects.hanoipetadoption.di

import com.projects.hanoipetadoption.data.network.ApiService
import com.projects.hanoipetadoption.data.repository.AdoptionRepository
import com.projects.hanoipetadoption.data.repository.PetRepositoryImpl
import com.projects.hanoipetadoption.data.source.PetLocalDataSource
import com.projects.hanoipetadoption.data.source.PetLocalDataSourceImpl
import com.projects.hanoipetadoption.data.source.PetRemoteDataSource
import com.projects.hanoipetadoption.data.source.PetRemoteDataSourceImpl
import com.projects.hanoipetadoption.domain.repository.PetRepository
import com.projects.hanoipetadoption.domain.usecase.GetAdoptedPetsUseCase
import com.projects.hanoipetadoption.domain.usecase.GetPetByIdUseCase
import com.projects.hanoipetadoption.domain.usecase.GetPetsUseCase
import com.projects.hanoipetadoption.domain.usecase.TogglePetFavoriteUseCase
import com.projects.hanoipetadoption.ui.viewmodel.PetDetailViewModel
import com.projects.hanoipetadoption.ui.viewmodel.PetsViewModel
import com.projects.hanoipetadoption.ui.viewmodel.PetViewModel
import com.projects.hanoipetadoption.util.ConnectivityChecker
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Main application module for dependency injection
 */
val appModule = module {
    // Utilities
    single { ConnectivityChecker(androidContext()) }
    
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
    single<PetLocalDataSource> { PetLocalDataSourceImpl(get(), get(), get(), get()) }
    single<PetRemoteDataSource> { PetRemoteDataSourceImpl(get()) }

    // Repositories
    single<PetRepository> { PetRepositoryImpl(get(), get(), get()) }
    single { AdoptionRepository(get()) }

    // Use Cases
    factory { GetPetByIdUseCase(get()) }
    factory { GetPetsUseCase(get()) }
    factory { TogglePetFavoriteUseCase(get()) }
    factory { GetAdoptedPetsUseCase(get(), get()) }

    // ViewModels
    viewModel { PetDetailViewModel(get(), get()) }
    viewModel { PetsViewModel(get()) }
    viewModel { PetViewModel(get(), get()) }
    
    // Data initialization
    single {
        // Initialize database with sample data if needed
        val localDataSource = get<PetLocalDataSource>()
        if (localDataSource is com.projects.hanoipetadoption.data.source.PetLocalDataSourceImpl) {
            kotlinx.coroutines.runBlocking {
                localDataSource.initializeWithSampleDataIfEmpty()
            }
        }
        // Return something for the dependency graph
        Unit
    }
}
