package com.projects.hanoipetadoption.di

import com.projects.hanoipetadoption.ui.viewmodel.myadoptedpets.AdoptedPetHubViewModel
import com.projects.hanoipetadoption.ui.viewmodel.myadoptedpets.MyAdoptedPetsViewModel
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.CareInstructionsViewModel
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.HealthTrackerViewModel
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.PetStatusViewModel
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.ReminderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    // ViewModels
    viewModel { MyAdoptedPetsViewModel(get()) }
    viewModel { AdoptedPetHubViewModel() }
    viewModel { HealthTrackerViewModel(get(), get(), get()) }
    viewModel { ReminderViewModel(get(), get(), get(), get()) }
    viewModel { CareInstructionsViewModel(get(), get()) }
    viewModel { PetStatusViewModel(get(), get()) }
}