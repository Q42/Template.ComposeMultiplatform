package nl.q42.template.navigation

import OnboardingScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import nl.q42.template.core.navigation.Route
import nl.q42.template.core.navigation.viewmodel.NavigatorImpl
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
}