package nl.q42.template.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.NavigatorImpl

@OptIn(InternalSerializationApi::class)
@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Destination.Home::class, Destination.Home::class.serializer())
                    subclass(Destination.InteropExamples::class, Destination.InteropExamples::class.serializer())
                }
            }
        },
        Destination.Home
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
            navEntryForKey(key = key, navigator = navigator)
        }
    )
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

