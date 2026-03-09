# SKILL.md

> All code produced by these skills must follow the conventions defined in [AGENTS.MD](../../../AGENTS.MD).

## Usage

Skills are invoked via Copilot chat using natural language. Each skill also has a canonical command format for precision.

You can say things like:
- *"Create a new screen called Settings in feature/home"*
- *"Add a Profile screen to the onboarding feature"*

Or use the canonical format:

```
Create a new screen called <Name> in feature/<module>
```

**Example:**

> Create a new screen called Settings in feature/home

Produces:
```
feature/home/src/commonMain/kotlin/nl/q42/template/feature/home/presentation/SettingsViewState.kt
feature/home/src/commonMain/kotlin/nl/q42/template/feature/home/presentation/SettingsViewModel.kt
feature/home/src/commonMain/kotlin/nl/q42/template/feature/home/ui/SettingsScreen.kt
feature/home/src/commonMain/kotlin/nl/q42/template/feature/home/ui/SettingsContent.kt
feature/home/src/commonMain/kotlin/nl/q42/template/feature/home/di/HomeModule.kt  ← updated
```

---

## Skill: create-screen

**Description:** Scaffolds a complete screen in a feature module: ViewState, ViewModel, Screen, Content, and Koin registration.

**Input:** Screen name (e.g. `Settings`) and target feature module path (e.g. `feature/home`). The package segment should use Kotlin dot notation (e.g. `feature.home`), not slashes.

**Steps** (execute in order):

### 1. `XViewState.kt` — in `presentation/`

```kotlin
package nl.q42.template.<package>.presentation

import nl.q42.template.core.ui.presentation.ViewStateString

sealed interface XViewState {
    data class Content(/* ViewStateString fields, no raw strings */) : XViewState
    data object Loading : XViewState
    data object Error : XViewState
}
```

- Use `ViewStateString` for any user-facing strings in `Content` — never raw `String`.
- Add only the fields `Content` actually needs; keep it minimal.

---

### 2. `XViewModel.kt` — in `presentation/`

```kotlin
package nl.q42.template.<package>.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import nl.q42.template.core.navigation.viewmodel.RouteNavigator
import nl.q42.template.core.ui.presentation.dialog.DialogPresenter

class XViewModel(
    private val navigator: RouteNavigator,
    private val dialogPresenter: DialogPresenter,
    // inject use cases here
) : ViewModel(), RouteNavigator by navigator, DialogPresenter by dialogPresenter {

    private val _uiState = MutableStateFlow<XViewState>(XViewState.Loading)
    val uiState: StateFlow<XViewState> = _uiState.asStateFlow()

    fun onScreenResumed() { }
}
```

- Always delegate `RouteNavigator` and `DialogPresenter`.
- All dependencies injected (no manual instantiation).
- State mutations always go through `_uiState.value = ...`.
- Use `viewModelScope.launch` for coroutines; use `handleAction` for `ActionResult`/`ApiResult`.

---

### 3. `XScreen.kt` — in `ui/`

```kotlin
package nl.q42.template.<package>.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.q42.template.core.ui.compose.OnLifecycleResume
import nl.q42.template.core.ui.compose.composables.dialog.InitDialogPresenter
import nl.q42.template.core.ui.compose.composables.window.ScaffoldWithAppBar
import nl.q42.template.<package>.presentation.XViewModel

@Composable
fun XScreen(viewModel: XViewModel) {

    OnLifecycleResume(viewModel::onScreenResumed)
    InitDialogPresenter(dialogPresenter = viewModel)

    val viewState by viewModel.uiState.collectAsStateWithLifecycle()

    ScaffoldWithAppBar(
        title = "X",
        onNavIconClicked = viewModel::navigateUp,
        content = { insetsPadding ->
            XContent(
                viewState = viewState,
                insetsPadding = insetsPadding,
            )
        },
    )
}
```

- **No business logic here.** Collect state, wire lambdas, delegate to `XContent`.
- Pass only `viewState` and lambda callbacks to `XContent` — never the ViewModel itself.
- Use `collectAsStateWithLifecycle` (not `collectAsState`).

---

### 4. `XContent.kt` — in `ui/`.

```kotlin
package nl.q42.template.<package>.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.q42.template.core.ui.theme.PreviewAppTheme
import nl.q42.template.<package>.presentation.XViewState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun XContent(
    viewState: XViewState,
    insetsPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    when (viewState) {
        is XViewState.Content -> { /* render content */ }
        is XViewState.Loading -> { /* render loading */ }
        is XViewState.Error -> { /* render error */ }
    }
}

@Preview
@Composable
private fun XContentPreview() {
    PreviewAppTheme {
        XContent(
            viewState = XViewState.Content(/* dummy data */),
            insetsPadding = PaddingValues(),
        )
    }
}
```

- No need to fill in /*  render content */ etc. Just keep the comment.
- **No ViewModel reference** — only `viewState` and event lambdas.
- Always `internal`.
- Always include a `@Preview` using `PreviewAppTheme`.
- `Modifier` parameter always last with default `= Modifier`.

---

### 5. Register in the feature's Koin DI module — in `di/`

Add `viewModelOf(::XViewModel)` to the existing feature module:

```kotlin
@OptIn(KoinExperimentalAPI::class)
val homeModule = module {
    // ...existing registrations...
    viewModelOf(::XViewModel)
}
```

- Use `viewModelOf()` — never `single` or `factory` for ViewModels.
- If no module file exists yet, create `di/XModule.kt` following the same pattern as `HomeModule.kt`.

---

### 6. Add destination to `Destination.kt`

Add a new `data object` entry inside `Destination` in `core/navigation/src/commonMain/kotlin/nl/q42/template/core/navigation/Destination.kt`:

```kotlin
@Serializable
sealed class Destination {
    // ...existing destinations...

    @Serializable
    data object X : Destination()
}
```

- Always annotate with `@Serializable`.
- Use PascalCase matching the screen name (e.g. `Settings`, `Profile`).

---

### 7. Add composable route in the navigation graph

Register the new screen's composable in the appropriate graph file under `composeApp/src/commonMain/kotlin/nl/q42/template/navigation/`.

**If the screen belongs to an existing graph** (e.g. `HomeGraph`), add a `composable` block inside that graph:

```kotlin
// In HomeGraph.kt (or the relevant graph file)
composable<Destination.X> {

    val viewModel: XViewModel = koinViewModel()
    InitNavigator(navController = navController, routeNavigator = viewModel)

    XScreen(viewModel)
}
```

**If the screen is a standalone destination** (not nested in a graph), create a new destinations file (e.g. `XDestinations.kt`) following the pattern of `OnboardingDestinations.kt`:

```kotlin
package nl.q42.template.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import nl.q42.template.core.navigation.Destination
import nl.q42.template.<package>.presentation.XViewModel
import nl.q42.template.<package>.ui.XScreen
import org.koin.compose.viewmodel.koinViewModel

internal fun NavGraphBuilder.xDestinations(navController: NavHostController) {
    composable<Destination.X> {

        val viewModel: XViewModel = koinViewModel()
        InitNavigator(navController = navController, viewModel)

        XScreen(viewModel = viewModel)
    }
}
```

Then register it in `App.kt` inside the `NavHost` block:

```kotlin
NavHost(
    navController = navController,
    startDestination = Destination.HomeGraph
) {
    homeGraph(navController = navController)
    onboardingDestinations(navController)
    xDestinations(navController)  // ← add this
}
```

- Determine placement by the feature module: screens in `feature/home` go in `HomeGraph.kt`; screens in other features get their own destinations file.
- Always import `koinViewModel` from `org.koin.compose.viewmodel.koinViewModel`.
- Always call `InitNavigator` to wire up ViewModel-driven navigation.

