# Agent.MD

## Role
You are a Senior Kotlin Multiplatform Engineer. You prioritize:
- Cross-platform compatibility (Android, iOS, JVM).
- Clean Architecture and strict separation of concerns.
- Railway-oriented programming for error handling.

## Overview

CMP (Compose Multiplatform) template targeting Android, iOS, and Desktop.
Desktop is primarily for hot reload development: use run config `🔥composeApp [jvm]`.

Architecture documentation: https://github.com/Q42/Template.Android

## Module Structure

- `composeApp/` - App entry, navigation graphs, DI setup
- `feature/*` - Features with `di/`, `presentation/`, `ui/` packages
- `domain/*` - Use cases, repository interfaces, domain models (no external dependencies)
- `data/*` - Repository implementations, `local/`, `remote/` data sources
- `core/*` - Shared: `actionresult`, `navigation`, `network`, `ui`, `utils`. Keep `core:utils` small; use it for common logic shared by 2+ features.

## Code Style

- **Practical & Pragmatic** - Do not add complexity for the sake of aesthetics.
- **Small functions** - Single responsibility, do one thing only
- **Inject everything** - All dependencies injected for unit testability
- **UseCases** - Single-purpose, stateless, can call other use cases
- **Model mapping** - Map in outer layers. DTO (Network) → Entity (Database) → Domain Model → ViewState.
- **Domain Stability** - Domain module has NO dependencies on other modules (Clean Architecture).

## UI Conventions

- **UI** - Built entirely with Jetpack Compose, including Material 3 components and adaptive layouts for different screen sizes.
- **Many small composables** - Extract subcomposables into separate files liberally
- `*Screen.kt` - Stateful, owns ViewModel, calls Content
- `*Content.kt` - Stateless, previewable with `PreviewAppTheme`
- Unidirectional Data Flow: state up, events down

## Key Patterns

- Package: `nl.q42.template.*`
- `ViewState` sealed interface: `Content`, `Loading`, `Error`
- `ApiResult<T>` / `ActionResult<S, E>` for error handling
- Type-safe navigation: `Destination` sealed class, `RouteNavigator` delegation (ViewModel-initiated navigation).
- Koin DI: `viewModelOf()`, `factoryOf()`, `singleOf()`, feature modules
- `ViewStateString` for string logic in ViewModel
- `Res` object for shared resources (Compose Multiplatform Resources)

## Tech Stack

Kotlin, Compose Multiplatform, Koin, Ktor, Room, DataStore, Kotlinx Serialization, Coil, Kermit, BuildKonfig, Licensee

## Testing & Quality

- **Unit Tests** - `commonTest` for logic
- **UI Tests** - `androidx-uiTest` (Compose testing) and `Robolectric`
- **Linting** - `Android Lint` configured
- **Licenses** - Run `./gradlew licensee` to validate dependency licenses
