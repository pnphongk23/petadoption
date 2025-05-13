# Active Context

## Current Session Goals
- Implementing post-adoption tracking and support features
- Setting up UI components for pet care instructions, health tracking, and reminders
- Implementing community support features
- Integrating with backend APIs defined in openapi.json

## Current Decisions
- Using MVVM architecture for all feature implementations
- CameraX API for photo/video capture
- ExoPlayer for video playback
- PdfRenderer for PDF viewing
- Timeline-based UI for post-adoption tracking
- Push notifications for reminders
- Using Repository pattern with remote and local data sources
- Implementing sample data for development and testing

## Session State
- Memory Bank system initialized on May 3, 2025
- Post-adoption tracking features implementation in progress
- Care instructions components implementation completed (data models, repositories, data sources)
- Backend API integration with OpenAPI specification

## Open Questions
- Should we implement Room database for care instructions persistence?
- How to implement offline-first approach for care instructions?
- How to handle large video uploads efficiently?
- Authentication flow for community support features?
- Should we create mappers for HealthRecordResponse to HealthRecord domain models?

## Active Files
- CareInstructions.kt (Data model for care instructions)
- DocumentType.kt (Enum for document types)
- HealthRecord.kt (Health record model classes)
- HealthRecordResponse.kt (API response models for health records)
- HealthRecordMapper.kt (Mappers for health record models)
- Result.kt (Result wrapper utility)
- HealthRecordRepository.kt and HealthRecordRepositoryImpl.kt (Repository implementation)
- HealthRecordRemoteDataSource.kt and HealthRecordRemoteDataSourceImpl.kt (Remote data source)
- CareRemoteDataSource.kt and CareRemoteDataSourceImpl.kt (Remote data source)
- CareLocalDataSource.kt and CareLocalDataSourceImpl.kt (Local data source)
- CareRepository.kt and CareRepositoryImpl.kt (Repository implementation)
- PostAdoptionModule.kt (Dependency injection for post-adoption features)
- PostAdoptionApiService.kt (API service for post-adoption features)