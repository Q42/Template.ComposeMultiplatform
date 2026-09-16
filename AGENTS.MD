# AGENTS.md
AGENTS.md
## Role
You are a Senior Kotlin Multiplatform Engineer. You prioritize:
- Cross-platform compatibility (Android, iOS, JVM).
- Clean Architecture and strict separation of concerns.
- Railway-oriented programming for error handling.

## Overview

CMP (Compose Multiplatform) template targeting Android, and iOS

Architecture documentation: https://github.com/Q42/Template.Android

## Module Structure

- `shared/` - Shared KMP app logic, navigation graphs, DI setup
- `feature/*` - Features with `di/`, `presentation/`, `ui/` packages
- `domain/*` - Use cases, repository interfaces, domain models (no external dependencies)
- `data/*` - Repository implementations, `local/`, `remote/` data sources
- `core/*` - Shared: `actionresult`, `navigation`, `network`, `ui`, `utils`. Keep `core:utils` small; use it for common logic shared by 2+ features.

## Code Style

- **Practical & Pragmatic** - Do not add complexity for the sake of aesthetics.
- **Small functions** - Single responsibility, do one thing only
- **Inject everything** - All dependencies injected for unit testability
- **UseCases** - Single-purpose, stateless, can call other use cases
- **Model mapping** - Map in outer layers. DTO (Network) → Domain Model → ViewState.
- **Domain Stability** - Domain module has NO dependencies on other modules (Clean Architecture).

## UI Conventions

- **UI** - Built entirely with Jetpack Compose, including Material 3 components and adaptive layouts for different screen sizes.
- **Many small composables** - Extract subcomposables into separate files liberally
- `*Screen.kt` - Stateful, owns ViewModel, calls Content
- `*Content.kt` - Stateless, previewable with `PreviewAppTheme`
- Unidirectional Data Flow: state up, events down

## Template Sample Code — `sample` prefix

This template ships with a fully working example flow (`feature/samplehome`, `feature/sampleonboarding`, the `SampleUser` domain/data slice, and a native-interop demo) so it runs and demos out of the box. Every file, class, package segment, Gradle module, and Koin module belonging to that example is marked with a `sample`/`Sample` prefix (e.g. `feature/samplehome`, `SampleUser`, `SampleFetchUserUseCase`, `sampleHomeModule`).

**`sample`/`Sample` is an example marker, not a project naming convention.**

- Any new feature, screen, use case, repository, module, etc. built for real product work must **never** use the `sample`/`Sample` prefix — name it after what it actually is (`feature/profile`, `FetchProfileUseCase`, `profileModule`), exactly like the naming examples in the [skills](.copilot/skills/) already do.
- If you see the prefix elsewhere in this codebase, treat it as a signal that the code is disposable example content to learn from, not a pattern to copy.
- **Never rename, repurpose, or extend an existing sample feature to "become" a real one.** If asked to build something that overlaps with a sample feature (e.g. a "Home" feature/ViewModel when `feature/samplehome` already exists), create a brand-new feature without the prefix (`feature/home`) and use the sample one only as a reference for the pattern — don't touch it. This applies to whatever sample features exist at the time (currently home, onboarding — more may be added later); each stays a disposable example, never a starting point to fill in.

## Skills

See SKILL.md files in the [skills folders](.copilot/skills/) for available agent skills and how to invoke them.

## Key Patterns

- Package: `nl.q42.template.*`
- `ViewState` sealed interface: `Content`, `Loading`, `Error`
- `ApiResult<T>` / `ActionResult<S, E>` for error handling
- Type-safe navigation: `Destination` sealed class with injected `Navigator` for ViewModel-initiated navigation.
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
