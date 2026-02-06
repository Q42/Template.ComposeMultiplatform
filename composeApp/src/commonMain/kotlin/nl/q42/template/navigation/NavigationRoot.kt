package nl.q42.template.navigation

import OnboardingScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import nl.q42.template.core.navigation.Route
import nl.q42.template.core.navigation.viewmodel.NavigatorImpl
import nl.q42.template.core.ui.compose.composables.widgets.AppButton
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.feature.home.ui.HomeScreen
import nl.q42.template.feature.home.ui.InteropExamplesScreen

@OptIn(InternalSerializationApi::class)
@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Home::class, Route.Home::class.serializer())
                    subclass(Route.InteropExamples::class, Route.InteropExamples::class.serializer())
                }
            }
        },
        Route.Home
    )

    val navigator = NavigatorImpl(navigationBackStack = backStack)

    NavDisplay(
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        transitionSpec = transitionSpec(),
        popTransitionSpec = popTransitionSpec(),
        predictivePopTransitionSpec = predictivePopTransitionSpec(),
        entryProvider = { key ->
            when (key) {
                Route.Home -> {
                    NavEntry(key) {
                        HomeScreen(navigator = navigator)
                    }
                }
                Route.Onboarding -> {
                    NavEntry(key) {
                        OnboardingScreen(navigator = navigator)
                    }
                }
                Route.InteropExamples -> {
                    NavEntry(key) {
                        InteropExamplesScreen(navigator = navigator)
                    }
                }
                else -> error("Unknown NavKey: $key")
            }
        }
    )
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by rememberSaveable { mutableStateOf(false) }
    if (showSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showSheet = false },
            modifier = Modifier
                .padding(top = Dimens.screenPaddingVertical)
        ) {
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dimens.buttonSpacingVertical, Alignment.CenterVertically),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text("This is a modal sheet example.")
                AppButton(
                    "Close modal",
                    onClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                            showSheet = false
                        }
                    }
                )
            }
        }
    }
}

private fun transitionSpec(): AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
    slideInHorizontally { it } + fadeIn() togetherWith
            slideOutHorizontally { -it } + fadeOut()
}

private fun popTransitionSpec(): AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
    slideInHorizontally { -it } + fadeIn() togetherWith
            slideOutHorizontally { it } + fadeOut()
}

private fun predictivePopTransitionSpec(): AnimatedContentTransitionScope<Scene<NavKey>>.(Int) -> ContentTransform = {
    slideInHorizontally { -it } + fadeIn() togetherWith
            slideOutHorizontally { it } + fadeOut()
}

