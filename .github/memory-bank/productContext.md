# Product Context

This document maintains high-level project context and knowledge for the Hanoi Pet Adoption application.

## Project Overview
The Hanoi Pet Adoption application is an Android mobile app designed to facilitate pet adoption in Hanoi. The app connects potential adopters with pets available for adoption from shelters and rescues.

## Key Features
- Pet browsing with filtering and search capabilities
- User profiles for potential adopters
- Shelter and rescue organization profiles
- Adoption application submission and tracking
- Favorite/saved pets functionality
- Notification system for application updates
- Admin dashboard for shelters
- Post-adoption tracking and support system:
  - Care instructions with integrated PDF viewer and video player
  - Pet status tracking with photo/video updates
  - Health tracking journal
  - Vaccination and care reminders
  - Community support and vet assistance

## User Personas
- **Pet Adopter**: Individuals looking to adopt a pet
- **Shelter Admin**: Staff managing shelter profiles and pet listings
- **Rescue Volunteer**: People fostering animals and managing rescue profiles
- **App Administrator**: System administrators managing the platform

## Technical Context
- Android application using Kotlin
- Modern Android development practices
- Gradle build system with Kotlin DSL
- MVVM architecture for UI components
- Repository pattern for data access
- Material Design for UI components
- Coroutines for asynchronous operations

## Component Architecture
- **UI Layer**: Activities, Fragments, ViewModels, and View Binding
- **Domain Layer**: Use Cases and Business Logic
- **Data Layer**: Repositories, Data Sources, and DTOs
- **Utility Layer**: Helpers, Extensions, and Utilities

## Integration Points
- Firebase Authentication for user management
- Firebase Firestore for data storage
- Firebase Storage for image storage
- Google Maps API for location-based features
- Notification services for reminders and alerts
- CameraX API for photo/video capture
- ExoPlayer for video playback
- PdfRenderer for care instructions
- Health Records API (from OpenAPI specification)
- Reminders API (from OpenAPI specification)
- Community forum integration ("YÊU CÚN CỎ")

## Non-Functional Requirements
- Offline functionality for basic pet browsing
- Fast loading times for pet listings (< 2 seconds)
- Responsive UI across different device sizes
- Battery-efficient background processes
- User-friendly error handling
- Accessibility features for diverse users