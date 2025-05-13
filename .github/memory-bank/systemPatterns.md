# System Patterns

This document records recurring patterns, coding standards, and architectural approaches used in the Hanoi Pet Adoption project.

## Architecture Patterns
### MVVM Architecture
The project follows the Model-View-ViewModel (MVVM) architecture pattern:

- **Model**: Data and business logic layers
  - Repository pattern for data access
  - Entity classes for database models
  - DTOs for network responses

- **View**: User interface components
  - Activities and Fragments
  - XML layouts with View Binding
  - Material Design components

- **ViewModel**: Connection between Model and View
  - Holds UI-related data
  - Handles UI logic
  - Survives configuration changes

### Repository Pattern
- Single source of truth for data
- Abstracts network and local data sources
- Provides clean API to ViewModels
- Handles caching strategy
- Uses Result wrapper for operation results
- Includes proper error handling and fallback mechanisms

### Dependency Injection
- Using Hilt for dependency injection
- Modules for providing dependencies
- Component scopes aligned with Android lifecycle

## UI Patterns
### Post-Adoption Tracking UI
- Timeline-based UI for pet updates
- Card-based layout for pet status entries
- Integrated camera controls for photo/video capture
- PDF viewer for care instructions
- Health log with categorized entries

### Media Handling
- Camera integration with CameraX API
- Image gallery with GridLayout
- Video playback with ExoPlayer
- PDF viewing with PdfRenderer

### Reminder System
- Calendar-style view for upcoming reminders
- Push notification integration

## Data Access Patterns
### Post-Adoption Repository Structure
- Two-tier data source architecture (Remote + Local)
- Remote data source for API communication
- Local data source for caching and offline support
- Repository as single source of truth
- Result wrapper for error handling

### Document Handling
- Document type enumeration with extension and MIME type helpers
- Local file caching using app-specific storage
- FileProvider for secure document sharing
- Streaming download for efficient document retrieval
- Categorized reminder types (vaccination, checkup, etc.)

### Community Support UI
- Chat interface with RecyclerView
- Contact cards for community resources
- Integration with external community forums

## Coding Standards
### Android/Kotlin Best Practices
- Follow Kotlin coding conventions
- Use view binding for UI interactions
- Implement MVVM architecture for UI components
- Use coroutines for asynchronous operations
- Flow for reactive data streams
- Follow Material Design guidelines

### Project Structure
- Feature-based package organization
- Clear separation of concerns between layers
- Resource naming conventions to follow Android standards

### Navigation
- Single Activity with multiple fragments
- Jetpack Navigation Component
- Bottom navigation for main sections
- Proper up navigation handling

## Testing Approach
- Unit tests for business logic
- UI tests for critical user flows
- Repository tests with mock data sources
- Integration tests for key features

## Documentation Standards
- KDoc comments for public APIs
- README files for major components
- Memory Bank updates for significant changes
- User documentation for key features