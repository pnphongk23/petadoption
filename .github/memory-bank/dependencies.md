<!-- filepath: e:\Git\hanoipetadoption\.github\memory-bank\dependencies.md -->
# Project Dependencies

This document tracks the third-party libraries and tools used in the Hanoi Pet Adoption project.

## Core Dependencies

### Android Framework
- AndroidX Core KTX
- AppCompat
- Material Components
- Constraint Layout
- Navigation Components (Jetpack)
- Lifecycle Components (ViewModel, LiveData)

### Networking
- Retrofit for API communication
- OkHttp for HTTP client
- Gson for JSON parsing

### Asynchronous Programming
- Kotlin Coroutines
- Flow for reactive streams

### Image Loading and Media
- Glide or Coil for image loading and caching
- CameraX for photo and video capture
- ExoPlayer for video playback
- PdfRenderer for PDF viewing

### Database
- Room for local database storage
- Firebase Firestore for cloud storage

### Authentication
- Firebase Authentication

### Analytics and Monitoring
- Firebase Analytics
- Firebase Crashlytics

### Testing
- JUnit for unit testing
- Espresso for UI testing
- Mockk for mocking in Kotlin
- Robolectric for Android unit tests

## Dependency Management

### Version Catalogs
Using Gradle Version Catalog (libs.versions.toml) to manage dependency versions centrally.

### Key Dependencies Versions
- Kotlin: TBD
- Coroutines: TBD
- AndroidX Core: TBD
- Material: TBD
- Navigation: TBD
- Retrofit: TBD
- Room: TBD
- Firebase: TBD

## Notes
- All dependencies should be kept up to date
- Security vulnerabilities should be regularly checked
- Prefer official Google libraries for Android development
- Use stable versions for production code
