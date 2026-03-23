package nl.q42.template.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

    /* The rememberNavBackStack is automatically persisted across process death and configuration
    * changes. So it can be passed into ViewModels, which outlive the views.
    *
    * This means that our NavKeys (Routes) need to be serialized and deserialized to be able to save
    * and restore the back stack. On native Android, Nav 3 can use reflection to automatically
    * derive the required serializers for the NavKeys, but on KMP this is not possible because
    * reflection is a little more limited here. Therefore, we need to provide the serializers for
    * each NavKey manually, using the serializersModule.
    */
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Destination.Home::class, Destination.Home::class.serializer())
                    subclass(Destination.InteropExamples::class, Destination.InteropExamples::class.serializer())
                    subclass(Destination.Onboarding::class, Destination.Onboarding::class.serializer())
                }
            }
        },
        Destination.Home
    )

    val navigator = remember { NavigatorImpl(navigationBackStack = backStack) }

    /*
     * EntryDecorators:
     * rememberSavableStateHolderNavEntryDecorator: required to make sure the backstack is properly
     * persisted across config changes.
     *
     * rememberViewModelStoreNavEntryDecorator: required to make sure that viewModels are properly
     * scoped to the corresponding views and cleared when the view is removed from the backstack.
    */
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

