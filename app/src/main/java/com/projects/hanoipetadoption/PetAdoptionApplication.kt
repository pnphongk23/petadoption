package com.projects.hanoipetadoption

import android.app.Application
import com.projects.hanoipetadoption.di.appModule
import com.projects.hanoipetadoption.di.postAdoptionModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application class to initialize Koin
 */
class PetAdoptionApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin
        startKoin {
            // Use Android logger to log Koin events - using Level.ERROR to avoid too many logs
            androidLogger(Level.ERROR)            // Declare Android context
            androidContext(this@PetAdoptionApplication)
            // Declare modules
            modules(listOf(appModule, postAdoptionModule))
        }
    }
}
